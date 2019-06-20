package com.ts.apigateway.mensajeria;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
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

@Component("categoriaMsgAdapter")
public class CategoriaMsgAdapterImpl implements CategoriaMsgAdapter {

	private static final String EXCHANGE_NAME="categoria_exchange";

	private static final String ROUTE_KEY_LIST ="categoria.lista";
	private static final String ROUTE_KEY_CREATE="categoria.crear";

	private static final Log LOGGER = LogFactory.getLog(CategoriaMsgAdapterImpl.class);

	@Override
	public void send(Categoria categoria) {

		try {
			Channel channel = RabbitMQ.getChannel();

			channel.exchangeDeclare(EXCHANGE_NAME,"direct");

			LOGGER.info("Creando exchange: " + EXCHANGE_NAME);

			byte[] data = (new Gson().toJson(categoria)).getBytes(StandardCharsets.UTF_8);

			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_CREATE, null, data);
			LOGGER.info("[x] Enviando por exchange '"+ EXCHANGE_NAME +"' por ruta '" +ROUTE_KEY_CREATE+"' ->" + new Gson().toJson(categoria));

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
			channel.exchangeDeclare(EXCHANGE_NAME,"direct");

			String correlation_id = UUID.randomUUID().toString();

			//Queue de respuesta (Queue aleatoria)
			String receiver_queue = channel.queueDeclare().getQueue();
			LOGGER.info("Creando queue receptora: " + receiver_queue);

			AMQP.BasicProperties properties = new AMQP.BasicProperties
					.Builder()
					.correlationId(correlation_id)
					.replyTo(receiver_queue)
					.build();

			//Publicacion hacia exchange con ruta adecuada
			channel.basicPublish(EXCHANGE_NAME, ROUTE_KEY_LIST, properties, null);
			LOGGER.info("[x] Solicitando lista categorias por exchange '"+ EXCHANGE_NAME+"' por ruta '"+ ROUTE_KEY_LIST +"'");

			//RECEPCION DE MENSAJES DESDE MSCATEGORIA
			BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

			String ctag = channel.basicConsume(receiver_queue, true, (consumerTag, delivery) -> {

				if (delivery.getProperties().getCorrelationId().equals(correlation_id)) {
					response.offer(new String(delivery.getBody(), StandardCharsets.UTF_8));
				}

			}, consumerTag -> {
			});

			String json = response.take();

			Type listType = new TypeToken<ArrayList<Categoria>>() {
			}.getType();

			categoriaList = new Gson().fromJson(json, listType);
			channel.basicCancel(ctag);

			LOGGER.info("[x] Recibido por queue '" + receiver_queue +"' -> " + categoriaList.toString());

		} catch (IOException | NoSuchAlgorithmException | URISyntaxException | TimeoutException | InterruptedException | KeyManagementException e) {
			e.printStackTrace();
		}

		return categoriaList;

	}

}
