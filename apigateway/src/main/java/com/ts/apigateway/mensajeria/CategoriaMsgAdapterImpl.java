package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.ts.apigateway.modelo.Categoria;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

@Component("mensajero")
public class CategoriaMsgAdapterImpl implements CategoriaMsgAdapter {

	private static final String QUEUE_NAME = "categoria_request";
	private static final Log LOGGER = LogFactory.getLog(CategoriaMsgAdapterImpl.class);
	private ConnectionFactory factory;

	@Override
	public void send(Categoria categoria) {

		try {
			Channel channel = RabbitMQ.getChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			LOGGER.info("Creando queue: " + QUEUE_NAME);

			//byte[] data = SerializationUtils.serialize(categoria);
			byte[] data = (new Gson().toJson(categoria)).getBytes(StandardCharsets.UTF_8);

			channel.basicPublish("", QUEUE_NAME, null, data);
			LOGGER.info("[x] Enviando por queue: " + new Gson().toJson(categoria));

		} catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Categoria> getList() {

		//OPERACION RPC
		List<Categoria> categoriaList=new ArrayList<>();

		try {

			Channel channel = RabbitMQ.getChannel();

			String correlation_id = UUID.randomUUID().toString();

			//Queue de respuesta
			String response_queue = channel.queueDeclare().getQueue();

			AMQP.BasicProperties properties = new AMQP.BasicProperties
					.Builder()
					.correlationId(correlation_id)
					.replyTo(response_queue)
					.build();

			channel.basicPublish("", "rpc_queue", properties, null);

			BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

			String ctag = channel.basicConsume(response_queue, true, (consumerTag, delivery) -> {

				if (delivery.getProperties().getCorrelationId().equals(correlation_id)) {
					response.offer(new String(delivery.getBody()));
				}

			}, consumerTag -> {
			});

			String json = response.take();

			Type listType = new TypeToken<ArrayList<Categoria>>() {
			}.getType();

			categoriaList = new Gson().fromJson(json, listType);
			channel.basicCancel(ctag);

			LOGGER.info("[x] Recibido por queue" + response_queue +": " + categoriaList.toString());

		} catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | InterruptedException | KeyManagementException e) {
			e.printStackTrace();
		}

		return categoriaList;

	}

}
