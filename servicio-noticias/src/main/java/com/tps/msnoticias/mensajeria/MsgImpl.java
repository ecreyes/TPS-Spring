package com.tps.msnoticias.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tps.msnoticias.dominio.NoticiaRoot;
import com.tps.msnoticias.servicio.NoticiaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Component("msgAdapter")
public class MsgImpl implements Msg {

  //NOTICIA
  private static final String EXCHANGE_NAME = "noticia_exchange";

  private static final String ROUTE_KEY_CREATE = "noticia.crear";
  private static final String ROUTE_KEY_EDIT = "noticia.editar";
  private static final String ROUTE_KEY_DELETE = "noticia.eliminar";

  private static final String ROUTE_KEY_LIST = "noticia.lista";

  private static final String QUEUE_REQUEST_CUD = "noticia_request_cud";
  private static final String QUEUE_REQUEST_LIST = "noticia_request_list";

  //CATEGORIA
  private static final String EXCHANGE_NAME_CAT = "categoria_exchange";
  private static final String ROUTE_KEY_LIST_CAT = "categoria.lista";
  private static final String ROUTE_KEY_LIST_CAT_SUBS = "noticia.lista.suscritos";
  private static final String QUEUE_REQUEST_LIST_CAT = "noticia_susc_categoria";
  private String jsonCategoriaList;

  private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);

  private final NoticiaService noticiaService;

  public MsgImpl(@Qualifier("noticiaService") NoticiaService noticiaService) {
    this.noticiaService = noticiaService;
  }

  /**
   * Proceso de mensajeria encargado de CREAR, ACTUALIZAR y ELIMINAR noticias. (Suscripcion)
   */
  @Override
  public void procesarCUD() {
    try {
      Channel channel = RabbitMQ.getConnection().createChannel();
      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String receiverQueue = channel.queueDeclare(QUEUE_REQUEST_CUD, true, false, false, null)
          .getQueue();

      LOGGER.info("Creando queue: " + receiverQueue);

      //Configuracion de rutas
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_CREATE);
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_EDIT);
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_DELETE);

      LOGGER.info(
          "[*] Esperando por solicitudes de (Creacion - Edicion - Eliminacion) de noticias. Para salir "
              +
              "presiona CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        switch (delivery.getEnvelope().getRoutingKey()) {

          //Solicitudes de creacion de noticias
          case ROUTE_KEY_CREATE: {

            NoticiaRoot noticiaRoot = new NoticiaRoot(jsonObject.get("titular").getAsString(),
                jsonObject.get("descripcion").getAsString(), jsonObject.get("autor").getAsString(),
                jsonObject.get("url").getAsString(), jsonObject.get("fuente").getAsString(),
                jsonObject.get("id_categoria").getAsInt());

            LOGGER.info(
                "[x] Recibido por queue '" + receiverQueue + "' -> " + noticiaRoot.toString());

            //Persistir noticia
            noticiaService.agregar(noticiaRoot);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            break;
          }

          //Solicitudes de edicion de noticias
          case ROUTE_KEY_EDIT: {

            NoticiaRoot noticiaRoot = new NoticiaRoot(jsonObject.get("id").getAsInt(),
                jsonObject.get(
                    "titular").getAsString(),
                jsonObject.get("descripcion").getAsString(), jsonObject.get("autor").getAsString(),
                jsonObject.get("url").getAsString(), jsonObject.get("fuente").getAsString(),
                jsonObject.get("id_categoria").getAsInt());

            LOGGER.info(
                "[x] Recibido por queue '" + receiverQueue + "' -> " + noticiaRoot.toString());

            noticiaService.editar(noticiaRoot);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            break;
          }

          //Solicitudes de eliminacion de noticias
          case ROUTE_KEY_DELETE: {

            NoticiaRoot noticiaRoot = new NoticiaRoot(jsonObject.get("id").getAsInt());

            LOGGER.info(
                "[x] Recibido por queue '" + receiverQueue + "' -> " + noticiaRoot.toString());

            //eliminar noticia
            noticiaService.eliminar(noticiaRoot);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            break;
          }
        }
      };

      boolean autoAck = false;
      channel.basicConsume(receiverQueue, autoAck, deliverCallback, (consumerTag) -> {
      });

    } catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
      e.printStackTrace();
    }
  }

  /**
   * Proceso de mensajeria encargado de enviar listado de noticias. (Request-Response Sincronico a
   * solicitudes desde Apigateway)
   */
  @Override
  public void procesarListadoNoticias() {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");
      String receiverQueue = channel.queueDeclare(QUEUE_REQUEST_LIST, false, false, false, null)
          .getQueue();

      //CONFIGURACION DE RUTAS
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_LIST);

      LOGGER.info("Creando queue: " + receiverQueue);
      LOGGER.info("[*] Esperando por solicitudes de lista noticias. Para salir presiona CTRL+C");

      Object monitor = new Object();

      //RECEPCION DE SOLICITUDES
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("[x] Solicitud de listado de noticias desde '" + json + "'");

        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
            .Builder()
            .correlationId(delivery.getProperties().getCorrelationId())
            .build();

        List<NoticiaRoot> noticiaRootList = noticiaService.obtenerNoticias(jsonCategoriaList);

        //Construccion JSON
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (NoticiaRoot noticiaRoot : noticiaRootList) {

          Map<String, Object> map = new HashMap<>();
          map.put("id", noticiaRoot.getId());
          map.put("titular", noticiaRoot.getTitular());
          map.put("descripcion", noticiaRoot.getDescripcion());
          map.put("autor", noticiaRoot.getAutor());
          map.put("url", noticiaRoot.getUrl());
          map.put("fuente", noticiaRoot.getFuenteNoticiaVO().getFuente());
          map.put("categoria", noticiaRoot.getCategoriaNoticiaVO().getNombre());

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

  /**
   * Proceso de mensajeria encargado de suscribirse y recibir actualizaciones de listado de
   * categorias enviadas desde Mscategoria (Suscripcion)
   */
  @Override
  public void procesarListadoCategorias() {

    //Solicitar datos en primera partida
    jsonCategoriaList = solicitarListadoCategorias();

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME_CAT, "direct");

      String receiverQueue = channel
          .queueDeclare(QUEUE_REQUEST_LIST_CAT, false, false, false, null).getQueue();

      LOGGER.info("Creando queue: " + receiverQueue);

      //Configuracion de rutas
      channel.queueBind(receiverQueue, EXCHANGE_NAME_CAT, ROUTE_KEY_LIST_CAT_SUBS);

      LOGGER.info("[*] Esperando por actualizaciones de categorias. Para salir presiona CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        String json = new String(delivery.getBody(), StandardCharsets.UTF_8);

        LOGGER.info("[x] Recibido por queue '" + receiverQueue + "' -> " + json);

        jsonCategoriaList = json;

      };
      channel.basicConsume(receiverQueue, true, deliverCallback, (consumerTag) -> {
      });
    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
      e.printStackTrace();
    }
  }

  /**
   * Funcion encargada de solicitar listado de categorias a MsCategoria cuando MsNoticia se
   * encuentra en partida en frio. (Request-response) Hacia mscategoria
   */
  private String solicitarListadoCategorias() {

    String json = "";
    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME_CAT, "direct");

      String correlationId = UUID.randomUUID().toString();

      //Queue de respuesta (Queue aleatoria)
      String receiverQueue = channel.queueDeclare().getQueue();
      LOGGER.info("Creando queue receptora: " + receiverQueue);

      AMQP.BasicProperties properties = new AMQP.BasicProperties
          .Builder()
          .correlationId(correlationId)
          .replyTo(receiverQueue)
          .build();

      String consumer = "msnoticia";
      byte[] data = consumer.getBytes(StandardCharsets.UTF_8);

      //Publicacion hacia exchange con ruta adecuada
      channel.basicPublish(EXCHANGE_NAME_CAT, ROUTE_KEY_LIST_CAT, properties, data);
      LOGGER.info(
          "[x] Solicitando lista categorias por exchange '" + EXCHANGE_NAME_CAT + "' por ruta '"
              + ROUTE_KEY_LIST_CAT + "'");

      //RECEPCION DE MENSAJES DESDE MSCATEGORIA
      BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

      String ctag = channel.basicConsume(receiverQueue, true, (consumerTag, delivery) -> {

        if (delivery.getProperties().getCorrelationId().equals(correlationId)) {
          response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
        }

      }, consumerTag -> {
      });

      json = response.take();
      channel.basicCancel(ctag);

      LOGGER.info("[x] Recibido por queue '" + receiverQueue + "' -> " + json);

    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | InterruptedException | KeyManagementException e) {
      e.printStackTrace();
    }

    return json;
  }

}
