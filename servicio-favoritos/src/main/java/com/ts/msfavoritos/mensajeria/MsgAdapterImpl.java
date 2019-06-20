package com.ts.msfavoritos.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ts.msfavoritos.repositorio.entidad.Favorito;
import com.ts.msfavoritos.servicio.FavoritoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("msgAdapter")
public class MsgAdapterImpl implements MsgAdapter {

    private static final String EXCHANGE_NAME = "favorito_exchange";

    private static final String ROUTE_KEY_CREATE = "favorito.crear";

    private static final String QUEUE_REQUEST_CREATE = "favorito_request_create";

    private static final Log LOGGER = LogFactory.getLog(MsgAdapterImpl.class);

    private final FavoritoService favoritoService;

    public MsgAdapterImpl(@Qualifier("favoritoService") FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @Override
    public void processFavorite() {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CREATE, false, false, false, null).getQueue();
            LOGGER.info("Creando queue: " + receiver_queue);

            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);

            LOGGER.info("[*] Esperando por solicitudes de creacion de favoritos. Para salir presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Favorito favorito = new Gson().fromJson(json, Favorito.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + favorito.toString());

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                favoritoService.agregarFavorito(favorito);
            };

            boolean autoAck = false;
            channel.basicConsume(receiver_queue, autoAck, deliverCallback, consumerTag -> {
            });


        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
