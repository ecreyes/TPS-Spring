package com.tps.msnoticias.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.service.NoticiaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("msgAdapter")
public class MsgAdapterImpl implements MsgAdapter {

    private static final String EXCHANGE_NAME = "noticia_exchange";

    private static final String ROUTE_KEY_CREATE = "noticia.crear";
    private static final String ROUTE_KEY_EDIT = "noticia.editar";
    private static final String ROUTE_KEY_DELETE = "noticia.eliminar";

    private static final String QUEUE_REQUEST_CUD = "noticia_request_cud";

    private static final Log LOGGER = LogFactory.getLog(MsgAdapterImpl.class);

    private final NoticiaService noticiaService;

    public MsgAdapterImpl(@Qualifier("noticiaService") NoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }


    @Override
    public void processCUD() {
        try{
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

                switch (delivery.getEnvelope().getRoutingKey()) {
                    case ROUTE_KEY_CREATE:
                        String json = new String(delivery.getBody());
                        Noticia noticia = new Gson().fromJson(json, Noticia.class);

                        LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticia.toString());
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        //Persistir noticia
                        noticiaService.agregarNoticia(noticia);
                        break;
                    case ROUTE_KEY_EDIT:
                        String json3 = new String(delivery.getBody());
                        Noticia noticia3 = new Gson().fromJson(json3, Noticia.class);
                        LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticia3.toString());
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        noticiaService.editarNoticia(noticia3);

                        break;
                    case ROUTE_KEY_DELETE:
                        String json2 = new String(delivery.getBody());
                        Noticia noticia2 = new Gson().fromJson(json2, Noticia.class);
                        LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticia2.toString());
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                        //eliminar noticia
                        noticiaService.eliminarNoticia(noticia2.getId());
                        break;
                }
            };

            boolean autoAck = false;
            channel.basicConsume(receiver_queue, autoAck, deliverCallback, (consumerTag) -> {
            });

        }catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

}
