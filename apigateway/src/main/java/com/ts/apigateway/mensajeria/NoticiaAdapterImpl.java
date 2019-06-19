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


@Component("NoticiaAdapter")
public class NoticiaAdapterImpl implements NoticiaAdapter {

    private static final String EXCHANGE_NAME="noticia_exchange";

    private static final String ROUTE_KEY_CREATE="noticia.crear";

    private static final Log LOGGER = LogFactory.getLog(NoticiaAdapterImpl.class);

    @Override
    public void send(Noticia noticia) {

        try{
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(noticia)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME,ROUTE_KEY_CREATE,null,data);

            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + ROUTE_KEY_CREATE + "' ->" + new Gson().toJson(noticia));

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
