package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ts.apigateway.modelo.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Component("usuarioMsgAdapter")
public class UsuarioMsgImpl implements UsuarioMsg {

    private static final String EXCHANGE_NAME = "usuario_exchange";

    private static final String ROUTE_KEY_LOGIN = "usuario.login";

    private static final Log LOGGER = LogFactory.getLog(UsuarioMsgImpl.class);

    /**
     * Envio de solicitudes de usuarios hacia exchange
     *
     * @param usuario   Objecto con datos de usuario enviado a MsUsuario
     * @param route_key Ruta utilizada para diferenciar operacion (Create,edit,delete)
     */
    @Override
    public void send(Usuario usuario, String route_key) {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(usuario)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME, route_key, MessageProperties.PERSISTENT_TEXT_PLAIN, data);
            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + route_key + "' ->" + new Gson().toJson(usuario));
        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion que solicita el login de un usuario en el sistema
     *
     * @param usuario Objecto usuario con datos
     * @return JSON con el estado de login
     */
    @Override
    public String requestLogin(Usuario usuario) {

        String json = "";

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String correlation_id = UUID.randomUUID().toString();

            //Queue de respuesta
            String receiver_queue = channel.queueDeclare().getQueue();
            LOGGER.info("Creando queue receptora: " + receiver_queue);

            AMQP.BasicProperties properties = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(correlation_id)
                    .replyTo(receiver_queue)
                    .build();

            byte[] data = (new Gson().toJson(usuario)).getBytes(StandardCharsets.UTF_8);

            //Publicacion
            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LOGIN, properties, data);
            LOGGER.info("[x] Solicitando login usuario por exchange '" + EXCHANGE_NAME + "' por ruta '" + ROUTE_KEY_LOGIN + "' ->" + new Gson().toJson(usuario));

            //Recepcion de datos desde msusuario
            BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            String ctag = channel.basicConsume(receiver_queue, false, (consumerTag, delivery) -> {

                if (delivery.getProperties().getCorrelationId().equals(correlation_id)) {
                    response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }, consumerTag -> {
            });

            json = response.take();

            channel.basicCancel(ctag);

            LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + json);

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException | InterruptedException e) {
            e.printStackTrace();
        }

        return json;
    }
}
