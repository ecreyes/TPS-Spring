package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.ts.apigateway.modelo.Noticia;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;


@Component("noticiaMsgAdapter")
public class NoticiaMsgAdapterImpl implements NoticiaMsgAdapter {

    private static final String EXCHANGE_NAME="noticia_exchange";

    private static final Log LOGGER = LogFactory.getLog(NoticiaMsgAdapterImpl.class);

    @Override
    public void send(Noticia noticia,String route_key) {

        try{
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(noticia)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME,route_key,null,data);

            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + route_key + "' ->" + new Gson().toJson(noticia));

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
