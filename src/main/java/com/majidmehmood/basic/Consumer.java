package com.majidmehmood.basic;

// import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.majidmehmood.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	public static void main(String[] args) {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("3.123.253.71");
		try {
			// optional: if not defined RabbitMQ will define and handle its own thread pool
			ExecutorService eService = Executors.newFixedThreadPool(10);
			Connection connection = factory.newConnection(eService);
			Channel channel = connection.createChannel();

			/// check out the Recipe 1 for publish message to the queue.
			channel.queueDeclare(Constants.QUEUE_NAME, Constants.DURABLE, Constants.EXCLUSIVE, Constants.AUTO_DELETE,
					null);

			ActualConsumer consumer = new ActualConsumer(channel);
			boolean autoAck = true;
			String consumerTag = channel.basicConsume(Constants.QUEUE_NAME, autoAck, consumer);
			System.out.println("Consumer Ready - press a key to terminate");
			System.in.read();

			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			eService.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
