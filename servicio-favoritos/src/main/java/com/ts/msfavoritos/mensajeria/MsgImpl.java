package com.ts.msfavoritos.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ts.msfavoritos.dominio.FavoritoRoot;
import com.ts.msfavoritos.servicio.FavoritoService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**Implementación de la interfaz Msg para realizar la comunicación a las colas**/
@Component("msgAdapter")
public class MsgImpl implements Msg {

  /**Nombre de las colas**/
  private static final String EXCHANGE_NAME = "favorito_exchange";

  private static final String ROUTE_KEY_CREATE = "favorito.crear";
  private static final String ROUTE_KEY_DELETE = "favorito.eliminar";
  private static final String ROUTE_KEY_LIST = "favorito.lista";

  private static final String QUEUE_REQUEST_CREATE = "favorito_request_cd";
  private static final String QUEUE_REQUEST_LIST = "favorito_request_list";

  private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);

  private final FavoritoService favoritoService;

  public MsgImpl(@Qualifier("favoritoService") FavoritoService favoritoService) {
    this.favoritoService = favoritoService;
  }

  /**
   * Proceso de mensajeria encargado de la Creacion y Eliminacion de noticias favoritas
   * (Suscripcion)
   */
  @Override
  public void procesarCD() {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String receiverQueue = channel.queueDeclare(QUEUE_REQUEST_CREATE, true, false, false, null)
          .getQueue();
      LOGGER.info("Creando queue: " + receiverQueue);

      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_CREATE);
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_DELETE);

      LOGGER.info(
          "[*] Esperando por solicitudes (Creacion - Eliminacion) de favoritos. Para salir presiona "
              +
              "CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        String json = new String(delivery.getBody(), StandardCharsets.UTF_8);

        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        int idNoticia = jsonObject.get("id_noticia").getAsInt();
        int idUsuario = jsonObject.get("id_usuario").getAsInt();

        //Solicitudes de creacion de favoritos
        if (delivery.getEnvelope().getRoutingKey().equals(ROUTE_KEY_CREATE)) {

          Date dateNow = new Date();

          SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String date_now_s = simpleDateFormat.format(dateNow);

          FavoritoRoot favoritoRoot = new FavoritoRoot(idUsuario, idNoticia, date_now_s);

          LOGGER.info(
              "[x] Recibido por queue '" + receiverQueue + "' -> " + favoritoRoot.toString());

          favoritoService.agregar(favoritoRoot);

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

        //Solicitudes de eliminacion de favoritos
        else if (delivery.getEnvelope().getRoutingKey().equals(ROUTE_KEY_DELETE)) {

          FavoritoRoot favoritoRoot = new FavoritoRoot(idUsuario, idNoticia);

          LOGGER.info(
              "[x] Recibido por queue '" + receiverQueue + "' -> " + favoritoRoot.toString());

          favoritoService.eliminar(favoritoRoot);

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        }
      };

      boolean autoAck = false;
      channel.basicConsume(receiverQueue, autoAck, deliverCallback, consumerTag -> {
      });


    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
      e.printStackTrace();
    }
  }

  /**
   * Proceso de mensajeria encargado de enviar el listado de noticias favoritas de un usuario
   * específico (Request-Response Sincronico a solicitudes desde Apigateway)
   */
  @Override
  public void procesarListaFavUsuario() {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String receiverQueue = channel.queueDeclare(QUEUE_REQUEST_LIST, false, false, false, null)
          .getQueue();

      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_LIST);

      LOGGER.info("Creando queue: " + receiverQueue);
      LOGGER.info(
          "[*] Esperando por solicitudes de lista favoritos de usuario. Para salir presiona CTRL+C");

      Object monitor = new Object();

      //RECEPCION DE SOLICITUDES
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        String json = new String(delivery.getBody(), StandardCharsets.UTF_8);

        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        LOGGER.info(
            "[x] Solicitud de listado de favoritos de usuario desde '" + jsonObject.get("consumer")
                .getAsString() + "'");

        int idUsuario = jsonObject.get("id_usuario").getAsInt();

        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
            .Builder()
            .correlationId(delivery.getProperties().getCorrelationId())
            .build();

        List<FavoritoRoot> favoritoRootList = favoritoService.getListaFavUsuario(idUsuario);

        //Construccion de JSON
        List<HashMap<String, Object>> mapList = new ArrayList<>();

        for (FavoritoRoot favoritoRoot : favoritoRootList) {

          HashMap<String, Object> map = new HashMap<>();
          map.put("id", favoritoRoot.getId());
          map.put("id_usuario", favoritoRoot.getUsuarioIdVO().getId());
          map.put("id_noticia", favoritoRoot.getNoticiaIdVO().getId());
          map.put("fecha_favorito", favoritoRoot.getFechaFavorito());

          mapList.add(map);
        }

        byte[] data = (new Gson().toJson(mapList).getBytes(StandardCharsets.UTF_8));

        //Enviarlo por cola unica (reply_to)
        channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, data);

        LOGGER.info(
            "[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + mapList
                .toString());

        synchronized (monitor) {
          monitor.notify();
        }
      };

      //En espera de solicitudes
      channel.basicConsume(receiverQueue, true, deliverCallback, (consumerTag) -> {
      });

      while (true) {
        synchronized (monitor) {
          try {
            monitor.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
      e.printStackTrace();
    }

  }
}
