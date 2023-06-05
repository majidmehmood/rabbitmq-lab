package com.majidmehmood.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.majidmehmood.basic.ActualConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Queue.BindOk;

public class Consumer {
  public static void main(String[] args) {

    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      String uri = "amqp://guest:guest@3.123.253.71:5672";
      factory.setUri(uri);
      connection = factory.newConnection();
      channel = connection.createChannel();
      channel.exchangeDeclare("bkn", "direct", false, true, null);
      String tempQueue = channel.queueDeclare().getQueue();
      BindOk queueBind = channel.queueBind(tempQueue, "bkn", "prd");

      String basicConsume = channel.basicConsume(tempQueue, true, new ActualConsumer(channel));

      channel.basicCancel(basicConsume);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (channel != null)
        try {
          channel.close();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (TimeoutException e) {
          e.printStackTrace();
        }
      if (connection != null)
        try {
          connection.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

  }
}
