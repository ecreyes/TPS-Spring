package com.ts.mscategoria.mensajeria;

import com.rabbitmq.client.ConnectionFactory;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class RabbitMQ {
    private static ConnectionFactory factory = null;

    public static ConnectionFactory getFactory() throws NoSuchAlgorithmException, KeyManagementException
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

}
