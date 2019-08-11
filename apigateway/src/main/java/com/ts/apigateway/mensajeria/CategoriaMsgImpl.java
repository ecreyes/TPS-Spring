package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ts.apigateway.modelo.Categoria;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component("categoriaMsgAdapter")
public class CategoriaMsgImpl implements CategoriaMsg {

  private static final String EXCHANGE_NAME = "categoria_exchange";
  private static final String ROUTE_KEY_LIST = "categoria.lista";

  private static final Log LOGGER = LogFactory.getLog(CategoriaMsgImpl.class);

  /**
   * Envio de solicitudes de categorias hacia Exchange (Publicacion)
   *
   * @param categoria Objecto categoria a ser enviado a Mscategoria
   * @param routeKey  Llave usada para identificar proceso.
   */
  @Override
  public void enviarMsg(Categoria categoria, String routeKey) {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

      byte[] data = (new Gson().toJson(categoria)).getBytes(StandardCharsets.UTF_8);

      channel.basicPublish(EXCHANGE_NAME, routeKey, MessageProperties.PERSISTENT_TEXT_PLAIN, data);
      LOGGER.info(
          "[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + routeKey + "' ->"
              + new Gson().toJson(categoria));

    } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
      e.printStackTrace();
    }

  }

  /**
   * Funcion de mensajeria encargada de obtener el listado de categorias desde Mscategoria.
   * (REQUEST-RESPONSE SINCRONICO) desde MsCategoria
   *
   * @return Listado de categorias
   */
  @Override
  public List<Categoria> obtenerListaCategorias() {

    List<Categoria> categoriaList = new ArrayList<>();

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();
      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String correlationId = UUID.randomUUID().toString();

      //Queue de respuesta (Queue aleatoria)
      String receiverQueue = channel.queueDeclare().getQueue();
      LOGGER.info("Creando queue receptora: " + receiverQueue);

      AMQP.BasicProperties properties = new AMQP.BasicProperties
          .Builder()
          .correlationId(correlationId)
          .replyTo(receiverQueue)
          .build();

      String consumer = "apigateway";
      byte[] data = consumer.getBytes(StandardCharsets.UTF_8);

      //Publicacion hacia exchange con ruta adecuada
      channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LIST, properties, data);
      LOGGER.info("[x] Solicitando lista categorias por exchange '" + EXCHANGE_NAME + "' por ruta '"
          + ROUTE_KEY_LIST + "'");

      //RECEPCION DE MENSAJES DESDE MSCATEGORIA
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

        Categoria categoria = new Categoria(jsonObject.get("id").getAsInt(),
            jsonObject.get("nombre").getAsString(), jsonObject.get("estado").getAsString());

        categoriaList.add(categoria);
      }

      LOGGER.info("[x] Recibido por queue '" + receiverQueue + "' -> " + categoriaList.toString());

    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | InterruptedException | KeyManagementException e) {
      e.printStackTrace();
    }
    return categoriaList;
  }

}
