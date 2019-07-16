package com.ts.mscategoria.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ts.mscategoria.dominio.CategoriaVO;
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

    private static final String EXCHANGE_NAME = "categoria_exchange";

    private static final String ROUTE_KEY_LIST = "categoria.lista";
    private static final String ROUTE_KEY_CREATE = "categoria.crear";
    private static final String ROUTE_KEY_EDIT = "categoria.editar";
    private static final String ROUTE_KEY_DELETE = "categoria.eliminar";

    private static final String QUEUE_REQUEST_CUD = "categoria_request_cud";
    private static final String QUEUE_REQUEST_LIST = "categoria_request_list";

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

                CategoriaVO categoriaVO = new Gson().fromJson(json, CategoriaVO.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + categoriaVO.toString());

                //Solicitudes de creacion de categorias
                switch (delivery.getEnvelope().getRoutingKey()) {
                    case ROUTE_KEY_CREATE:

                        Categoria categoria = new Categoria(categoriaVO.getNombre());

                        //Persistir categoria
                        categoriaService.agregarCategoria(categoria);

                        //Actualizar agregado
                        categoriaService.cargarAgregado(true);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        break;

                    //Solicitudes de edicion de categorias
                    case ROUTE_KEY_EDIT:

                        categoriaService.editarCategoria(categoriaVO);

                        categoriaService.cargarAgregado(true);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        break;

                    //Solicitudes de eliminar categorias
                    case ROUTE_KEY_DELETE:

                        categoriaService.eliminarCategoria(categoriaVO);

                        categoriaService.cargarAgregado(true);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
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

                LOGGER.info("[x] Solicitud de listado de categorias desde apigateway");

                AMQP.BasicProperties reply_props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                List<CategoriaVO> categoriaVOList = categoriaService.obtenerAgregado().getCategoriaVOList();
                byte[] data = (new Gson().toJson(categoriaVOList).getBytes(StandardCharsets.UTF_8));

                //Enviarlo por cola unica (reply_to)
                channel.basicPublish("", delivery.getProperties().getReplyTo(), reply_props, data);

                LOGGER.info("[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + categoriaVOList.toString());

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
}
