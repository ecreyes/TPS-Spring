package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ts.apigateway.modelo.Categoria;
import com.ts.apigateway.modelo.Favorito;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Component("favoritoMsgAdapter")
public class FavoritoMsgImpl implements FavoritoMsg {

    private static final String EXCHANGE_NAME = "favorito_exchange";

    private static final String ROUTE_KEY_LIST = "favorito.lista";

    private static final Log LOGGER = LogFactory.getLog(FavoritoMsgImpl.class);

    /**
     * Envío de solicitudes de creacion y eliminacion de favoritos hacia msfavorito
     * (Publicacion)
     *
     * @param favorito  Objecto favorito con datos
     * @param route_key Llave usada para identificar proceso
     */
    @Override
    public void enviarMsg(Favorito favorito, String route_key) {

        try {
            Channel channel = RabbitMQ.getConnection().createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(favorito)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME, route_key, MessageProperties.PERSISTENT_TEXT_PLAIN, data);
            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + route_key + "' ->" + new Gson().toJson(favorito));

        } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion de mensajeria encargada de obtener el listado de noticias favoritas de un usuario específico.
     * (REQUEST-RESPONSE SINCRONICO) desde MsFavorito
     *
     * @return Listado de favoritos
     */
    @Override
    public List<Favorito> obtenerListaFavoritosUsuario(String id_usuario) {

        List<Favorito> favoritoList = new ArrayList<>();

        try {
            Channel channel = RabbitMQ.getConnection().createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String correlation_id = UUID.randomUUID().toString();

            String receiver_queue = channel.queueDeclare().getQueue();

            LOGGER.info("Creando queue receptora: " + receiver_queue);

            AMQP.BasicProperties properties = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlation_id)
                    .replyTo(receiver_queue)
                    .build();


            String consumer = "apigateway";
            Map<String, Object> map = new HashMap<>();
            map.put("id_usuario", id_usuario);
            map.put("consumer", consumer);

            byte[] data = (new Gson().toJson(map)).getBytes(StandardCharsets.UTF_8);

            //Publicacion hacia exchange con ruta adecuada
            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LIST, properties, data);

            LOGGER.info("[x] Solicitando listado favoritos de usuario por exchange '" + EXCHANGE_NAME + "' por ruta " +
                    "'" + ROUTE_KEY_LIST + "'");

            //RECEPCION DE MENSAJES DESDE MSFAVORITO
            BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            String ctag = channel.basicConsume(receiver_queue, true, (consumerTag, delivery) -> {

                if (delivery.getProperties().getCorrelationId().equals(correlation_id)) {
                    response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
                }

            }, consumerTag -> {
            });

            String json = response.take();
            channel.basicCancel(ctag);

            //JSON PARSE
            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                Favorito favorito = new Favorito(jsonObject.get("id").getAsInt(),
                        jsonObject.get("id_usuario").getAsInt(), jsonObject.get("id_noticia").getAsInt(),
                        jsonObject.get("fecha_favorito").getAsString());

                favoritoList.add(favorito);
            }
            LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + favoritoList.toString());

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException | InterruptedException e) {
            e.printStackTrace();
        }

        return favoritoList;
    }
}
