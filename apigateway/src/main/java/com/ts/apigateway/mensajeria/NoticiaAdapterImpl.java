package com.ts.apigateway.mensajeria;
import com.google.gson.Gson;
import com.ts.apigateway.modelo.Noticia;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component("NoticiaAdapterImpl")
public class NoticiaAdapterImpl implements NoticiaAdapter {
    private static final String QUEUE_NAME = "noticia_request";
    private static final Log LOGGER = LogFactory.getLog(NoticiaAdapterImpl.class);

    @Override
    public void send(Noticia noticia) {
        byte[] data = (new Gson().toJson(noticia)).getBytes(StandardCharsets.UTF_8);
        LOGGER.info("Creando queue: " + QUEUE_NAME);
        if(RabbitMQ.sendData(QUEUE_NAME,data)){
            LOGGER.info("[x] Enviando por queue: " + new Gson().toJson(noticia));
        }else{
            LOGGER.info("No se pudo enviar el dato");
        }
    }

}
