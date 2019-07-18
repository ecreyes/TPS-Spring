package com.ts.msfavoritos.mensajeria;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ts.msfavoritos.dominio.FavoritoRoot;
import com.ts.msfavoritos.dominio.NoticiaIdVO;
import com.ts.msfavoritos.dominio.UsuarioIdVO;
import com.ts.msfavoritos.servicio.FavoritoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;

@Component("msgAdapter")
public class MsgImpl implements Msg {

    private static final String EXCHANGE_NAME = "favorito_exchange";

    private static final String ROUTE_KEY_CREATE = "favorito.crear";
    private static final String ROUTE_KEY_DELETE = "favorito.eliminar";

    private static final String QUEUE_REQUEST_CREATE = "favorito_request_cd";

    private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);

    private final FavoritoService favoritoService;

    public MsgImpl(@Qualifier("favoritoService") FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    /**
     * Proceso de mensajeria encargado de la Creacion y Eliminacion de noticias favoritas
     */
    @Override
    public void processCD() {

        try {
            Channel channel = RabbitMQ.getChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CREATE, true, false, false, null).getQueue();
            LOGGER.info("Creando queue: " + receiver_queue);

            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);
            channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_DELETE);

            LOGGER.info("[*] Esperando por solicitudes (Creacion - Eliminacion) de favoritos. Para salir presiona " +
                    "CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                String json = new String(delivery.getBody(), StandardCharsets.UTF_8);

                JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

                NoticiaIdVO noticiaIdVO = new NoticiaIdVO(jsonObject.get("id_noticia").getAsInt());
                UsuarioIdVO usuarioIdVO = new UsuarioIdVO(jsonObject.get("id_usuario").getAsInt());

                //Solicitudes de creacion de favoritos
                if (delivery.getEnvelope().getRoutingKey().equals(ROUTE_KEY_CREATE)) {

                    Date date_now = new Date();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date_now_s = simpleDateFormat.format(date_now);

                    FavoritoRoot favoritoRoot = new FavoritoRoot(usuarioIdVO, noticiaIdVO, date_now_s);

                    LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + favoritoRoot.toString());

                    favoritoService.agregarFavorito(favoritoRoot);

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }

                //Solicitudes de eliminacion de favoritos
                else if (delivery.getEnvelope().getRoutingKey().equals(ROUTE_KEY_DELETE)) {

                    FavoritoRoot favoritoRoot = new FavoritoRoot(usuarioIdVO, noticiaIdVO);

                    LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + favoritoRoot.toString());

                    favoritoService.eliminarFavorito(favoritoRoot);

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                }
            };

            boolean autoAck = false;
            channel.basicConsume(receiver_queue, autoAck, deliverCallback, consumerTag -> {
            });


        } catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
