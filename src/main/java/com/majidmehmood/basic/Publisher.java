package com.majidmehmood.basic;

import com.majidmehmood.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Publisher {
    public static void main(String[] args) {
        try {
            // TCP connection with default connection parameters user:guest, password:
            // guest, and vhost: /vhost is like a namespace containing connections,
            // exchanges and queues
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Constants.HOST);
            String uri = "amqp://guest:guest@localhost:5672";
            factory.setUri(uri);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel(); // channel is a logical session

            channel.queueDeclare(Constants.QUEUE_NAME, Constants.DURABLE, Constants.EXCLUSIVE, Constants.AUTO_DELETE,
                    null);

            String message = "normal message";
            channel.basicPublish("", Constants.QUEUE_NAME, null, message.getBytes());

            message = "persistent message";
            channel.basicPublish("", Constants.QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

            System.out.println("Message sent: " + message);

            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
