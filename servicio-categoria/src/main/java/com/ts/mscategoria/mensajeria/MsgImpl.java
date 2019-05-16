package com.ts.mscategoria.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.ts.mscategoria.dominio.CategoriaVO;
import com.ts.mscategoria.repositorio.entidad.Categoria;
import com.ts.mscategoria.servicio.CategoriaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("mensajero")
public class MsgImpl implements MsgAdapter {


	private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);
	private static final String QUEUE_NAME = "categoria_request";
	private ConnectionFactory factory;

	@Autowired
	@Qualifier("categoriaService")
	private CategoriaService categoriaService;

	//Debe ser implementado en servicio categoria
	@Override
	public void receive() {

		try {
			factory = setFactory();

			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();


			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			LOGGER.info("Creando queue: " + QUEUE_NAME);
			LOGGER.info("[*] Esperando por nuevos mensajes. Para salir presiona CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				//CategoriaVO categoriaVO = SerializationUtils.deserialize(delivery.getBody());
				String json = new String(delivery.getBody());
				Categoria categoria = new Gson().fromJson(json,Categoria.class);

				//assert categoriaVO != null;
				LOGGER.info("Recibido desde cola: " + categoria.toString());
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

				//Persistir categoria
				categoriaService.agregarCategoria(categoria);
			};

			boolean autoAck = false;
			channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, (consumerTag) -> {
			});

		} catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	private ConnectionFactory setFactory() throws NoSuchAlgorithmException, KeyManagementException
			, URISyntaxException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setRequestedHeartbeat(30);
		factory.setUri(
				"amqp://xuueptgg:hYmOJdYsGPSSW-rvY_WSRXB1OK2YW8II@fox.rmq.cloudamqp.com/xuueptgg");

		return factory;
	}
}
