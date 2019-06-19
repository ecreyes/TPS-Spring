package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.ts.apigateway.modelo.Usuario;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Component("usuarioMsgAdapter")
public class UsuarioMsgAdapterImpl implements UsuarioMsgAdapter {

    private static final String EXCHANGE_NAME = "usuario_exchange";

    private static final String ROUTE_KEY_CREATE = "usuario.crear";
    private static final String ROUTE_KEY_LOGIN = "usuario.login";

    private static final Log LOGGER = LogFactory.getLog(UsuarioMsgAdapterImpl.class);

    @Override
    public void send(Usuario usuario) {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

            byte[] data = (new Gson().toJson(usuario)).getBytes(StandardCharsets.UTF_8);

            channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_CREATE, null, data);
            LOGGER.info("[x] Enviando por exchange '" + EXCHANGE_NAME + "' por ruta '" + ROUTE_KEY_CREATE + "' ->" + new Gson().toJson(usuario));
        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> requestLogin(Usuario usuario) {

        Map<String, Object> map = new HashMap<>();

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

            BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            String ctag = channel.basicConsume(receiver_queue, false, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(correlation_id)) {
                    response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            }, consumerTag -> {
            });

            String json = response.take();

            channel.basicCancel(ctag);

            LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + json);

            //Procesado de resultado
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

            //JSON TO data
            String status = jsonObject.get("STATUS").getAsString();

            if (jsonObject.getAsJsonObject("Usuario") != null) {
                map.put("Usuario", jsonObject.get("Usuario").getAsJsonObject());
            }
            map.put("STATUS", status);

        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException | InterruptedException e) {
            e.printStackTrace();
        }

        return map;
    }
}
