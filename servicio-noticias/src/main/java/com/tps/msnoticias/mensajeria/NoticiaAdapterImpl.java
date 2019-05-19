package com.tps.msnoticias.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.tps.msnoticias.repository.entity.Noticia;
import com.tps.msnoticias.repository.service.NoticiaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("NoticiaAdapterImpl")
public class NoticiaAdapterImpl implements NoticiaAdapter {
    private ConnectionFactory factory;
    private static final String QUEUE_NAME = "noticia_request";
    private static final Log LOGGER = LogFactory.getLog(NoticiaAdapterImpl.class);
    @Autowired
    @Qualifier("noticiaServiceImp")
    private NoticiaService noticiaService;

    @Override
    public void receive() {
        try {
            factory = RabbitMQ.getFactory();

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();


            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            LOGGER.info("Creando queue: " + QUEUE_NAME);
            LOGGER.info("[*] Esperando por nuevos mensajes. Para salir presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String json = new String(delivery.getBody());
                Noticia noticia = new Gson().fromJson(json,Noticia.class);

                //assert categoriaVO != null;
                LOGGER.info("Recibido desde cola: " + noticia.toString());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                //Persistir categoria
                noticiaService.agregarNoticia(noticia);
            };

            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {
            });


        } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
}
