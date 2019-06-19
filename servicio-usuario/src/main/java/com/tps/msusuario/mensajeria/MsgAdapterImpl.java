package com.tps.msusuario.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.tps.msusuario.repositorio.entidad.Usuario;
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

@Component("MsgAdapter")
public class MsgAdapterImpl implements MsgAdapter {

    private static final String EXCHANGE_NAME = "usuario_exchange";
    private static final String ROUTE_KEY_CREATE = "usuario.crear";
    private static final String ROUTE_KEY_LOGIN = "usuario.login";

    private static final String QUEUE_REQUEST_CREATE = "usuario_request_create";
    private static final String QUEUE_REQUEST_LOGIN = "usuario_request_login";

    private static final Log LOGGER = LogFactory.getLog(MsgAdapterImpl.class);

    private final UsuarioService usuarioService;

    public MsgAdapterImpl(@Qualifier("usuarioService") UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void processCreate() {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CREATE, false, false, false, null).getQueue();

            LOGGER.info("Creando queue: " + receiver_queue);

            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);

            LOGGER.info("[*] Esperando por solicitudes de creacion de usuarios. Para salir presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody());
                Usuario usuario = new Gson().fromJson(json, Usuario.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + usuario.toString());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                //Persistir categoria
                usuarioService.agregarUsuario(usuario);

            };

            boolean autoAck = false;
            channel.basicConsume(receiver_queue, autoAck, deliverCallback, (consumerTag) -> {
            });
        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processLogin() {
        //RPC
        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_LOGIN, false, false, false, null).getQueue();

            LOGGER.info("Creando queue: " + receiver_queue);

            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_LOGIN);

            LOGGER.info("[*] Esperando por solicitudes de login. Para salir presiona CTRL+C");

            Object monitor = new Object();

            //RECEPCION DE SOLICITUDES
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                AMQP.BasicProperties reply_props = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                String json_received = new String(delivery.getBody(), StandardCharsets.UTF_8);
                Usuario usuario = new Gson().fromJson(json_received, Usuario.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + usuario.toString());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                //Realizar login
                Map<String,Object> result = usuarioService.loginUsuario(usuario);
                String json_result = new Gson().toJson(result);

                //Enviarlo por cola unica (reply_to)
                channel.basicPublish("", delivery.getProperties().getReplyTo(), reply_props, json_result.getBytes(StandardCharsets.UTF_8));

                LOGGER.info("[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + json_result);

                synchronized (monitor) {
                    monitor.notify();
                }
            };

            //En espera de solicitudes
            channel.basicConsume(receiver_queue, false, deliverCallback, (consumerTag) -> {
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
