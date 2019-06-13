package com.ts.mscategoria.mensajeria;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
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
public class MsgAdapterImpl implements MsgAdapter {

	private static final String EXCHANGE_NAME = "categoria_exchange";

	private static final String ROUTE_KEY_LIST = "categoria.lista";
	private static final String ROUTE_KEY_CREATE = "categoria.crear";

	private static final String QUEUE_REQUEST_CREATE = "categoria_request_create";
	private static final String QUEUE_REQUEST_LIST = "categoria_request_list";

	private static final Log LOGGER = LogFactory.getLog(MsgAdapterImpl.class);

	private final CategoriaService categoriaService;

	public MsgAdapterImpl(@Qualifier("categoriaService") CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}

	//Metodo que agrega cetegorias
	@Override
	public void processCreate() {

		try {
			Channel channel = RabbitMQ.getChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, "direct");

			String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_CREATE, false, false, false, null).getQueue();
			LOGGER.info("Creando queue: " + receiver_queue);

			channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_CREATE);

			LOGGER.info("[*] Esperando por solicitudes de creacion. Para salir presiona CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {

				String json = new String(delivery.getBody());
				Categoria categoria = new Gson().fromJson(json,Categoria.class);

				LOGGER.info("[x] Recibido por queue '" + receiver_queue + "' -> " + categoria.toString());
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

				//Persistir categoria
				categoriaService.agregarCategoria(categoria);

			};

			boolean autoAck = false;
			channel.basicConsume(QUEUE_REQUEST_CREATE, autoAck, deliverCallback, (consumerTag) -> {
			});

		} catch (NoSuchAlgorithmException | KeyManagementException | URISyntaxException | IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	//recibe peticion de listado y envia lista
	@Override
	public void processList() {

		try {
			Channel channel = RabbitMQ.getChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, "direct");

			String receiver_queue = channel.queueDeclare(QUEUE_REQUEST_LIST, false, false, false, null).getQueue();

			//Declarar bind para recibir mensajes con ruta "categoria.lista"
			channel.queueBind(receiver_queue, EXCHANGE_NAME, ROUTE_KEY_LIST);

			LOGGER.info("Creando queue: " + receiver_queue);
			LOGGER.info("[*] Esperando por solicitudes de lista categorias. Para salir presiona CTRL+C");


			Object monitor = new Object();

			//RECEPCION DE SOLICITUDES
			DeliverCallback deliverCallback = (consumerTag, delivery) -> {

				AMQP.BasicProperties reply_props = new AMQP.BasicProperties
						.Builder()
						.correlationId(delivery.getProperties().getCorrelationId())
						.build();

				List<Categoria> categoriaList = categoriaService.obtenerCategorias();

				byte[] data = (new Gson().toJson(categoriaList)).getBytes(StandardCharsets.UTF_8);

				//Enviarlo por cola unica (reply_to)
				channel.basicPublish("", delivery.getProperties().getReplyTo() , reply_props, data);

				LOGGER.info("[x] Enviando por queue '" + delivery.getProperties().getReplyTo() + "' -> " + categoriaList.toString());

				synchronized (monitor){
					monitor.notify();
				}
			};

			//En espera de solicitudes
			channel.basicConsume(receiver_queue, true, deliverCallback, (consumerTag) -> {
			});

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
