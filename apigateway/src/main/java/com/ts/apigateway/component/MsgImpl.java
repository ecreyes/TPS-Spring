package com.ts.apigateway.component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.ts.apigateway.model.Categoria;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Component("mensajero")
public class MsgImpl implements MsgAdapter {

	private static final String QUEUE_NAME = "categoria_request";
	private ConnectionFactory factory;

	@Override
	public void send(Categoria categoria) {

		try {
			factory = setFactory();
			Connection connection = factory.newConnection();

			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			byte[] data = SerializationUtils.serialize(categoria);

			channel.basicPublish("", QUEUE_NAME, null, data);
			System.out.println("[x] Enviando " + data);

			channel.close();
			connection.close();

		} catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
			e.printStackTrace();
		}

	}

	//Debe ser implementado en servicio categoria
	@Override
	public void receive() {

		try {
			factory = setFactory();

			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				Categoria categoria =
						(Categoria) SerializationUtils.deserialize(delivery.getBody());
				assert categoria != null;
				System.out.println("Recibido por cola" + categoria.toString());
			};

			channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag) -> {
			});
			channel.close();
			connection.close();

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
