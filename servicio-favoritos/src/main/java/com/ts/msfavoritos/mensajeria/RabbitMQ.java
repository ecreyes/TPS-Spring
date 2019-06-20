package com.ts.msfavoritos.mensajeria;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {
    private static ConnectionFactory factory = null;
    private static Connection connection = null;
    private static Channel channel=null;
    private static Log LOGGER = LogFactory.getLog(RabbitMQ.class);

    private static ConnectionFactory getFactory() throws NoSuchAlgorithmException, KeyManagementException
            , URISyntaxException {
        if(factory==null){
            ConnectionFactory factory = new ConnectionFactory();
            factory.setRequestedHeartbeat(30);
            factory.setUri(
                    "amqp://xuueptgg:hYmOJdYsGPSSW-rvY_WSRXB1OK2YW8II@fox.rmq.cloudamqp.com/xuueptgg");
            return factory;
        }else{
            return factory;
        }
    }

    private static Connection getConnection() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        if (connection == null) {
            connection = RabbitMQ.getFactory().newConnection();
            LOGGER.info("Creando conexion");
            return connection;
        } else {
            LOGGER.info("Reusando conexion");
            return connection;
        }
    }

    public static Channel getChannel() throws URISyntaxException, IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException {
        if(channel==null){
            channel = RabbitMQ.getConnection().createChannel();
            LOGGER.info("Creando canal");
            return channel;
        }
        else{
            LOGGER.info("Reusando canal");
            return channel;
        }
    }

}
