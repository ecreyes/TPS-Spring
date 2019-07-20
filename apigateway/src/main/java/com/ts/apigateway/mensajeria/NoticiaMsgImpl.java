package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ts.apigateway.modelo.Noticia;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;


@Component("noticiaMsgAdapter")
public class NoticiaMsgImpl implements NoticiaMsg {

    private static final String EXCHANGE_NAME = "noticia_exchange";
    private static final String ROUTE_KEY_LIST = "noticia.lista";

    private static final Log LOGGER = LogFactory.getLog(NoticiaMsgImpl.class);

    /**
     * Envio de solicitudes de noticia hacia Exchange
     * (Publicacion)
     *
     * @param noticia Objecto noticia a ser enviado a MsNoticia
     * @param route_key Llave usada para identificar proceso. (Crear,Eliminar,Editar)
     */
    @Override
    public void enviarMsg(Noticia noticia, String route_key) {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(noticia)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME, route_key, MessageProperties.PERSISTENT_TEXT_PLAIN, data);

            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + route_key + "' ->" + new Gson().toJson(noticia));

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion de mensajeria encargada de obtener el listado de noticias desde MsNoticia.
     * (REQUEST-RESPONSE SINCRONICO) desde MsNoticia
     *
     * @return Listado de noticias
     */
    @Override
    public List<Noticia> obtenerListadoNoticias() {

        List<Noticia> noticiaList = new ArrayList<>();
        try {
            Channel channel = RabbitMQ.getChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String correlation_id = UUID.randomUUID().toString();

            //Queue de respuesta (Queue aleatoria)
            String receiver_queue = channel.queueDeclare().getQueue();
            LOGGER.info("Creando queue receptora: " + receiver_queue);

            AMQP.BasicProperties properties = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlation_id)
                    .replyTo(receiver_queue)
                    .build();

            String consumer = "apigateway";
            byte[] data = consumer.getBytes(StandardCharsets.UTF_8);

            //Publicacion hacia exchange con ruta adecuada
            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LIST, properties, data);
            LOGGER.info("[x] Solicitando lista noticias por exchange '" + EXCHANGE_NAME + "' por ruta '" + ROUTE_KEY_LIST + "'");

            //RECEPCION DE MENSAJES DESDE MSNOTICIA
            BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            String ctag = channel.basicConsume(receiver_queue, true, (consumerTag, delivery) -> {

                if (delivery.getProperties().getCorrelationId().equals(correlation_id)) {
                    response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
                }

            }, consumerTag -> {
            });

            String json = response.take();
            channel.basicCancel(ctag);

            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                Noticia noticia = new Noticia(jsonObject.get("id").getAsInt(),
                        jsonObject.get("titular").getAsString(), jsonObject.get("descripcion").getAsString(),
                        jsonObject.get("autor").getAsString(), jsonObject.get("url").getAsString(),
                        jsonObject.getAsJsonObject("fuenteNoticiaVO").get("fuente").getAsString(),
                        jsonObject.getAsJsonObject("categoriaNoticiaVO").get("nombre").getAsString());

                noticiaList.add(noticia);
            }
            LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + noticiaList.toString());

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | InterruptedException | KeyManagementException e) {
            e.printStackTrace();
        }
        return noticiaList;
    }

}
