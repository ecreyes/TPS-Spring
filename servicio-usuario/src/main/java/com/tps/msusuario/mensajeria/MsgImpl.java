package com.tps.msusuario.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tps.msusuario.dominio.EstadoUsuarioVO;
import com.tps.msusuario.dominio.NombreUsuarioVO;
import com.tps.msusuario.dominio.UsuarioRoot;
import com.tps.msusuario.servicio.UsuarioService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeoutException;
/**Adaptador que implementa los metodos de Msg para permitir la comunicacion
 * de mensajes con rabbit**/
@Component("msgAdapter")
public class MsgImpl implements Msg {

  //Nombre de las colas que estan en rabbit para enviar mensajes
  private static final String EXCHANGE_NAME = "usuario_exchange";

  private static final String ROUTE_KEY_CREATE = "usuario.crear";
  private static final String ROUTE_KEY_DELETE = "usuario.eliminar";
  private static final String ROUTE_KEY_EDIT = "usuario.editar";
  private static final String ROUTE_KEY_LOGIN = "usuario.login";

  private static final String QUEUE_REQUEST_CUD = "usuario_request_cud";
  private static final String QUEUE_REQUEST_LOGIN = "usuario_request_login";

  private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);

  private final UsuarioService usuarioService;

  public MsgImpl(@Qualifier("usuarioService") UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  /**
   * Proceso de mensajeria encargado de CREAR, ACTUALIZAR y ELIMINAR usuarios. (Suscripcion)
   */
  @Override
  public void procesarCUD() {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String receiverQueue = channel.queueDeclare(QUEUE_REQUEST_CUD, true, false, false, null)
          .getQueue();

      LOGGER.info("Creando queue: " + receiverQueue);

      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_CREATE);
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_DELETE);
      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_EDIT);

      LOGGER.info(
          "[*] Esperando por solicitudes de (Creacion - Edicion - Eliminacion) de usuarios. Para salir presiona CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        String json = new String(delivery.getBody());
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        switch (delivery.getEnvelope().getRoutingKey()) {

          //Solicitud de creacion de usuario
          case ROUTE_KEY_CREATE: {

            //Construccion agregado
            UsuarioRoot usuarioRoot = new UsuarioRoot(jsonObject.get("username").getAsString(),
                jsonObject.get("email").getAsString(),
                jsonObject.get("password").getAsString(), jsonObject.get("estado").getAsString());

            LOGGER.info(
                "[x] Recibido por queue '" + receiverQueue + "' -> " + usuarioRoot.toString());

            usuarioService.agregar(usuarioRoot);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            break;
          }

          //Solicitudes de editar usuario
          case ROUTE_KEY_EDIT: {

            //Construccion agregado
            UsuarioRoot usuarioRoot = new UsuarioRoot(jsonObject.get("id").getAsInt(),
                jsonObject.get(
                    "username").getAsString(), jsonObject.get("email").getAsString(),
                jsonObject.get(
                    "password").getAsString(), jsonObject.get("estado").getAsString());

            LOGGER.info(
                "[x] Recibido por queue '" + receiverQueue + "' -> " + usuarioRoot.toString());

            usuarioService.editar(usuarioRoot);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            break;
          }

          //Solicitudes de eliminar usuarios
          case ROUTE_KEY_DELETE: {

            //Construccion agregado
            UsuarioRoot usuarioRoot = new UsuarioRoot(jsonObject.get("id").getAsInt());

            LOGGER.info(
                "[x] Recibido por queue '" + receiverQueue + "' -> " + usuarioRoot.toString());

            //Eliminar usuario
            usuarioService.eliminar(usuarioRoot);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            break;
          }
        }
      };

      boolean autoAck = false;
      channel.basicConsume(receiverQueue, autoAck, deliverCallback, (consumerTag) -> {
      });
    } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
      e.printStackTrace();
    }

  }

  /**
   * Proceso de mensajeria encargado del logear usuarios en el sistema (Request-Response Sincronico
   * a solicitudes desde Apigateway)
   */
  @Override
  public void procesarLogin() {

    try {
      Channel channel = RabbitMQ.getConnection().createChannel();

      channel.exchangeDeclare(EXCHANGE_NAME, "direct");

      String receiverQueue = channel.queueDeclare(QUEUE_REQUEST_LOGIN, false, false, false, null)
          .getQueue();

      LOGGER.info("Creando queue: " + receiverQueue);

      channel.queueBind(receiverQueue, EXCHANGE_NAME, ROUTE_KEY_LOGIN);

      LOGGER.info("[*] Esperando por solicitudes de login. Para salir presiona CTRL+C");

      Object monitor = new Object();

      //RECEPCION DE SOLICITUDES
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {

        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
            .Builder()
            .correlationId(delivery.getProperties().getCorrelationId())
            .build();

        String jsonReceived = new String(delivery.getBody(), StandardCharsets.UTF_8);
        UsuarioRoot usuarioRoot = new Gson().fromJson(jsonReceived, UsuarioRoot.class);

        LOGGER.info("[x] Recibido por queue '" + receiverQueue + "' -> " + usuarioRoot.toString());
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

        //Realizar login
        Map<String, Object> map = usuarioService.login(usuarioRoot);
        byte[] data = (new Gson().toJson(map)).getBytes(StandardCharsets.UTF_8);

        //Enviarlo por cola unica (reply_to)
        channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, data);

        LOGGER.info(
            "[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + map
                .toString());

        synchronized (monitor) {
          monitor.notify();
        }
      };

      //En espera de solicitudes
      channel.basicConsume(receiverQueue, false, deliverCallback, (consumerTag) -> {
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
