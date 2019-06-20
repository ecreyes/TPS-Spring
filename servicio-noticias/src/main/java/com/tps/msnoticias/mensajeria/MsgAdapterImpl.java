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

    private static final String QUEUE_REQUEST_CREATE = "noticia_request_create";

    private static final Log LOGGER = LogFactory.getLog(MsgAdapterImpl.class);

    private final NoticiaService noticiaService;

    public MsgAdapterImpl(@Qualifier("noticiaService") NoticiaService noticiaService) {
        this.noticiaService = noticiaService;
    }

    @Override
    public void processCreate() {
        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CREATE, false, false, false, null).getQueue();

            LOGGER.info("Creando queue: " + receiver_queue);

            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);

            LOGGER.info("[*] Esperando por solicitudes de creacion de noticias. Para salir presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody());
                Noticia noticia = new Gson().fromJson(json, Noticia.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticia.toString());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                //Persistir noticia
                noticiaService.agregarNoticia(noticia);
            };

            boolean autoAck = false;
            channel.basicConsume(receiver_queue, autoAck, deliverCallback, (consumerTag) -> {
            });


        } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
}
