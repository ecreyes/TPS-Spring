package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.ts.apigateway.modelo.Favorito;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("favoritoMsgAdapter")
public class FavoritoMsgAdapterImpl implements FavoritoMsgAdapter {

    private static final String EXCHANGE_NAME = "favorito_exchange";

    private static final String ROUTE_KEY_CREATE = "favorito.crear";

    private static final Log LOGGER = LogFactory.getLog(FavoritoMsgAdapterImpl.class);

    @Override
    public void send(Favorito favorito) {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(favorito)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_CREATE, null, data);
            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + ROUTE_KEY_CREATE + "' ->" + new Gson().toJson(favorito));

        } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
