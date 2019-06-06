package com.ts.mscategoria.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import com.ts.mscategoria.repositorio.entidad.Categoria;
import com.ts.mscategoria.servicio.CategoriaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component("mensajero")
public class MsgImpl implements MsgAdapter {


	private static final Log LOGGER = LogFactory.getLog(MsgImpl.class);
	private static final String QUEUE_NAME = "categoria_request";
	private static ConnectionFactory factory;

	private final CategoriaService categoriaService;

	public MsgImpl(@Qualifier("categoriaService") CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	//Debe ser implementado en servicio categoria
	@Override
	public void receive() {

		try {
			factory = RabbitMQ.getFactory();

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

	@Override
	public void sendList() {

		try {
			factory = RabbitMQ.getFactory();

			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare("rpc_queue", false, false, false, null);
			channel.queuePurge("rpc_queue");
			LOGGER.info("Creando queue: " + "rpc_queue");
			LOGGER.info("[*] Esperando por mensajes de lista. Para salir presiona CTRL+C");
			channel.basicQos(1);

			Object monitor = new Object();

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {

				AMQP.BasicProperties reply_props = new AMQP.BasicProperties
						.Builder()
						.correlationId(delivery.getProperties().getCorrelationId())
						.build();

				List<Categoria> categoriaList = categoriaService.obtenerCategorias();

				byte[] data = (new Gson().toJson(categoriaList)).getBytes(StandardCharsets.UTF_8);

				channel.basicPublish("", delivery.getProperties().getReplyTo() , reply_props, data);
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
				LOGGER.info("[x] Enviando por queue "+delivery.getProperties().getReplyTo() +": " + new Gson().toJson(categoriaList));

				synchronized (monitor){
					monitor.notify();
				}
			};

			//En espera de solicitudes
			channel.basicConsume("rpc_queue",false,deliverCallback,(consumerTag) -> {});

			while(true){
				synchronized (monitor){
					try{
						monitor.wait();
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
			}

		} catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | KeyManagementException e) {
			e.printStackTrace();
		}
	}
}
