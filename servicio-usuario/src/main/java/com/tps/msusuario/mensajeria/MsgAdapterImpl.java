package com.tps.msusuario.mensajeria;

import com.google.gson.Gson;
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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("mensajero")
public class MsgAdapterImpl implements MsgAdapter {

    private static final String EXCHANGE_NAME="usuario_exchange";
    private static final String ROUTE_KEY_CREATE="usuario.crear";

    private static final String QUEUE_REQUEST_CREATE="usuario_request_create";

    private static final Log LOGGER= LogFactory.getLog(MsgAdapterImpl.class);

    private final UsuarioService usuarioService;

    public MsgAdapterImpl(@Qualifier("usuarioService") UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void processCreate() {

        try{
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME,"direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CREATE,false,false,false,null).getQueue();

            LOGGER.info("Creando queue: " + receiver_queue);

            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);

            LOGGER.info("[*] Esperando por solicitudes de creacion. Para salir presiona CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody());
                Usuario usuario = new Gson().fromJson(json,Usuario.class);

                LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + usuario.toString());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                //Persistir categoria
                usuarioService.agregarUsuario(usuario);

            };

            boolean autoAck = false;
            channel.basicConsume(QUEUE_REQUEST_CREATE, autoAck, deliverCallback, (consumerTag) -> {
            });
        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
