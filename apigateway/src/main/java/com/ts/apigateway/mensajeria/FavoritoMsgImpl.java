package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ts.apigateway.modelo.Favorito;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component("favoritoMsgAdapter")
public class FavoritoMsgImpl implements FavoritoMsg {

  private static final String EXCHANGE_NAME = "favorito_exchange";

  private static final String ROUTE_KEY_LIST = "favorito.lista";

  private static final Log LOGGER = LogFactory.getLog(FavoritoMsgImpl.class);

  /**
   * Envío de solicitudes de creacion y eliminacion de favoritos hacia msfavorito (Publicacion)
   *
   * @param favorito Objecto favorito con datos
   * @param routeKey Llave usada para identificar proceso
   */
  @Override
  public void enviarMsg(Favorito favorito, String routeKey) {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

      HashMap<String, Object> map = new HashMap<>();
      map.put("id_usuario", favorito.getId_usuario());
      map.put("id_noticia", favorito.getId_noticia());

      byte[] data = (new Gson().toJson(map)).getBytes(StandardCharsets.UTF_8);

      channel.basicPublish(EXCHANGE_NAME, routeKey, MessageProperties.PERSISTENT_TEXT_PLAIN, data);
      LOGGER.info(
          "[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + routeKey + "' ->"
              + new Gson().toJson(favorito));

    } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  /**
   * Funcion de mensajeria encargada de obtener el listado de noticias favoritas de un usuario
   * específico. (REQUEST-RESPONSE SINCRONICO) desde MsFavorito
   *
   * @return Listado de favoritos
   */
  @Override
  public List<Favorito> obtenerListaFavoritosUsuario(String idUsuario) {

    List<Favorito> favoritoList = new ArrayList<>();

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String correlationId = UUID.randomUUID().toString();

      String receiverQueue = channel.queueDeclare().getQueue();

      LOGGER.info("Creando queue receptora: " + receiverQueue);

      AMQP.BasicProperties properties = new AMQP.BasicProperties
          .Builder()
          .correlationId(correlationId)
          .replyTo(receiverQueue)
          .build();

      String consumer = "apigateway";
      Map<String, Object> map = new HashMap<>();
      map.put("id_usuario", idUsuario);
      map.put("consumer", consumer);

      byte[] data = (new Gson().toJson(map)).getBytes(StandardCharsets.UTF_8);

      //Publicacion hacia exchange con ruta adecuada
      channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LIST, properties, data);

      LOGGER.info("[x] Solicitando listado favoritos de usuario por exchange '" + EXCHANGE_NAME
          + "' por ruta " +
          "'" + ROUTE_KEY_LIST + "'");

      //RECEPCION DE MENSAJES DESDE MSFAVORITO
      BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

      String ctag = channel.basicConsume(receiverQueue, true, (consumerTag, delivery) -> {

        if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
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
      LOGGER.info("[x] Recibido por queue '" + receiverQueue + "' -> " + favoritoList.toString());

    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException | InterruptedException e) {
      e.printStackTrace();
    }

    return favoritoList;
  }
}
