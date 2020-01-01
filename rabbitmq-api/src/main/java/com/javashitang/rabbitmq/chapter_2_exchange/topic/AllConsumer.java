package com.javashitang.rabbitmq.chapter_2_exchange.topic;

import com.javashitang.rabbitmq.chapter_2_exchange.direct.Producer4DirectExchange;
import com.rabbitmq.client.*;

import java.io.IOException;

public class AllConsumer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("www.javashitang.com");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 声明一个交换机
        channel.exchangeDeclare(Producer4TopicExchange.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String queueName = "all_queue";
        String bindingKey = "#";

        channel.queueDeclare(queueName, false, false, false ,null);
        channel.queueBind(queueName, Producer4DirectExchange.EXCHANGE_NAME, bindingKey);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("routingKey is " + envelope.getRoutingKey() + " message is "  + message);
            }
        };

        channel.basicConsume(queueName , true, consumer);
    }
}
