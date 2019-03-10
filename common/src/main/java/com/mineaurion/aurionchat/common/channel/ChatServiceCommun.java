package com.mineaurion.aurionchat.common.channel;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class ChatServiceCommun {
    private Connection connection;
    private Channel channel;
    private String consumerTag;

    private static String EXCHANGE_NAME = "aurions.chat";

    public ChatServiceCommun(String uri){
        ConnectionFactory factory = new ConnectionFactory();
        try{
            factory.setUri(uri);
        }
        catch (KeyManagementException|URISyntaxException|NoSuchAlgorithmException UriKeyException){
            desactivatePlugin();
            System.out.println("Uri Syntax Exception, please check the config or the documentation of rabbitmq");
        }
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);
        try{
            connection = factory.newConnection();
            channel = connection.createChannel();
        }
        catch (IOException|TimeoutException exception){
            System.out.println("Connection error with rabbitmq");
            System.out.println(exception.getMessage());
        }
    }

    public void join() throws IOException{
        if(this.consumerTag != null){
            channel.basicCancel(consumerTag);
        }
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "aurions.chat.*");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
          String channel = delivery.getEnvelope().getRoutingKey().split("\\.")[1];
          sendMessage(channel, message);
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void leave() throws IOException {
        channel.exchangeUnbind(EXCHANGE_NAME,"","aurions.chat.*");
        channel.queueUnbind(channel.queueDeclare().getQueue(), EXCHANGE_NAME,"aurions.chat.*");
    }

    public void send(String channelName,String message) throws IOException {
        channel.basicPublish(EXCHANGE_NAME,"aurions.chat." + channelName, null, message.getBytes());
    }
    public void close() throws TimeoutException, IOException{
        channel.close();
        connection.close();
    }

    abstract public void sendMessage(String channelName, String message);
    abstract public void desactivatePlugin();

}
