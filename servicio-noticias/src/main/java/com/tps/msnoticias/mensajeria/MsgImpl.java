package com.tps.msnoticias.mensajeria;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWithSerializerProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tps.msnoticias.dominio.FuenteNoticiaVO;
import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.service.NoticiaService;
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

    private static final String EXCHANGE_NAME = "noticia_exchange";

    private static final String ROUTE_KEY_CREATE = "noticia.crear";
    private static final String ROUTE_KEY_EDIT = "noticia.editar";
    private static final String ROUTE_KEY_DELETE = "noticia.eliminar";

    private static final String ROUTE_KEY_LIST = "noticia.lista";

    private static final String QUEUE_REQUEST_CUD = "noticia_request_cud";
    private static final String QUEUE_REQUEST_LIST = "noticia_request_list";

    private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);

    private final NoticiaService noticiaService;

    public MsgImpl(@Qualifier("noticiaService") NoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }

    /**
     * Proceso de mensajeria encargado de CREAR, ACTUALIZAR y ELIMINAR noticias.
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

            LOGGER.info("[*] Esperando por solicitudes de (Creacion - Edicion - Eliminacion) de noticias. Para salir " +
                    "presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

                switch (delivery.getEnvelope().getRoutingKey()) {

                    //Solicitudes de creacion de noticias
                    case ROUTE_KEY_CREATE: {

                        FuenteNoticiaVO fuenteNoticiaVO = new FuenteNoticiaVO(jsonObject.get("fuente").getAsString());
                        NoticiaRoot noticiaRoot = new NoticiaRoot(jsonObject.get("titular").getAsString(),
                                jsonObject.get("descripcion").getAsString(), jsonObject.get("autor").getAsString(),
                                jsonObject.get("url").getAsString(), fuenteNoticiaVO);

                        LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticiaRoot.toString());
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        //Persistir noticia
                        noticiaService.agregarNoticia(noticiaRoot);
                        break;
                    }

                    //Solicitudes de edicion de noticias
                    case ROUTE_KEY_EDIT: {

                        FuenteNoticiaVO fuenteNoticiaVO = new FuenteNoticiaVO(jsonObject.get("fuente").getAsString());
                        NoticiaRoot noticiaRoot = new NoticiaRoot(jsonObject.get("id").getAsInt(), jsonObject.get(
                                "titular").getAsString(),
                                jsonObject.get("descripcion").getAsString(), jsonObject.get("autor").getAsString(),
                                jsonObject.get("url").getAsString(), fuenteNoticiaVO);

                        LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticiaRoot.toString());

                        noticiaService.editarNoticia(noticiaRoot);

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        break;
                    }

                    //Solicitudes de eliminacion de noticias
                    case ROUTE_KEY_DELETE: {

                        NoticiaRoot noticiaRoot = new NoticiaRoot(jsonObject.get("id").getAsInt());

                        LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticiaRoot.toString());

                        //eliminar noticia
                        noticiaService.eliminarNoticia(noticiaRoot.getId());

                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        break;
                    }
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
            LOGGER.info("[*] Esperando por solicitudes de lista noticias. Para salir presiona CTRL+C");

            Object monitor = new Object();

            //RECEPCION DE SOLICITUDES
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                LOGGER.info("[x] Solicitud de listado de noticias desde apigateway");

                AMQP.BasicProperties reply_props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                List<NoticiaRoot> noticias = noticiaService.getNoticias();
                byte[] data = (new Gson().toJson(noticias).getBytes(StandardCharsets.UTF_8));

                //Enviarlo por cola unica (reply_to)
                channel.basicPublish("", delivery.getProperties().getReplyTo(), reply_props, data);

                LOGGER.info("[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + noticias.toString());

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
