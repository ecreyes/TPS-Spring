package com.ts.mscategoria.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ts.mscategoria.dominio.CategoriaRoot;
import com.ts.mscategoria.repositorio.entidad.Categoria;
import com.ts.mscategoria.servicio.CategoriaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component("msgAdapter")
public class MsgImpl implements Msg {

    //Categoria
    private static final String EXCHANGE_NAME = "categoria_exchange";

    private static final String ROUTE_KEY_LIST = "categoria.lista";
    private static final String ROUTE_KEY_CREATE = "categoria.crear";
    private static final String ROUTE_KEY_EDIT = "categoria.editar";
    private static final String ROUTE_KEY_DELETE = "categoria.eliminar";

    private static final String QUEUE_REQUEST_CUD = "categoria_request_cud";
    private static final String QUEUE_REQUEST_LIST = "categoria_request_list";

    //Noticia
    private static final String ROUTE_KEY_LIST_SUBS = "noticia.lista.subscribers";

    private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);

    private final CategoriaService categoriaService;

    public MsgImpl(@Qualifier("categoriaService") CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * Proceso de mensajeria encargado de CREAR, ACTUALIZAR y ELIMINAR categorias.
     */
    @Override
    public void processCUD() {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CUD, true, false, false, null).getQueue();

            LOGGER.info("Creando queue: " + receiver_queue);

            //Configuracion de rutas
            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);
            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_EDIT);
            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_DELETE);

            LOGGER.info("[*] Esperando por solicitudes de creacion de categorias. Para salir presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);

                CategoriaRoot categoriaRoot = new Gson().fromJson(json, CategoriaRoot.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + categoriaRoot.toString());

                //Solicitudes de creacion de categorias
                switch (delivery.getEnvelope().getRoutingKey()) {
                    case ROUTE_KEY_CREATE:

                        //Persistir categoria
                        categoriaService.agregar(categoriaRoot);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        //envia nueva lista a miscroservicios que lo requieran
                        sendList();

                        break;

                    //Solicitudes de edicion de categorias
                    case ROUTE_KEY_EDIT:

                        categoriaService.editar(categoriaRoot);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        //envia nueva lista a miscroservicios que lo requieran
                        sendList();

                        break;

                    //Solicitudes de eliminar categorias
                    case ROUTE_KEY_DELETE:

                        categoriaService.eliminar(categoriaRoot);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        //envia nueva lista a miscroservicios que lo requieran
                        sendList();

                        break;
                }
            };

            boolean autoAck = false;
            channel.basicConsume(receiver_queue, autoAck, deliverCallback, (consumerTag) -> {
            });

        } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * Proceso de mensajeria encargado de enviar listado de categorias
     * (Request-Response Sincronico a solicitudes de Apigateway)
     */
    @Override
    public void processList() {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_LIST, false, false, false, null).getQueue();

            //CONFIGURACION DE RUTAS
            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_LIST);

            LOGGER.info("Creando queue: " + receiver_queue);
            LOGGER.info("[*] Esperando por solicitudes de lista categorias. Para salir presiona CTRL+C");

            Object monitor = new Object();

            //RECEPCION DE SOLICITUDES
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);

                LOGGER.info("[x] Solicitud de listado de categorias desde '" + json + "'");

                AMQP.BasicProperties reply_props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                List<CategoriaRoot> categoriaRootList = categoriaService.getCategorias();

                byte[] data = (new Gson().toJson(categoriaRootList).getBytes(StandardCharsets.UTF_8));

                //Enviarlo por cola unica (reply_to)
                channel.basicPublish("", delivery.getProperties().getReplyTo(), reply_props, data);

                LOGGER.info("[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + categoriaRootList.toString());

                synchronized (monitor) {
                    monitor.notify();
                }
            };

            //En espera de solicitudes
            channel.basicConsume(receiver_queue, true, deliverCallback, (consumerTag) -> {
            });

            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion utilizada para enviar el listado de categorías a los miscroservicios que esten suscritos
     * a la actualizacion de categorias
     */
    private void sendList() {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            List<CategoriaRoot> categoriaRootList = categoriaService.getCategorias();

            byte[] data = (new Gson().toJson(categoriaRootList)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LIST_SUBS, null, data);

            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + ROUTE_KEY_LIST_SUBS + "' ->" + new Gson().toJson(categoriaRootList));

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
