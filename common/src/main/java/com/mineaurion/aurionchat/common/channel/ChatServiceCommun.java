package com.mineaurion.aurionchat.common.channel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public abstract class ChatServiceCommun {
    private Connection connection;
    private Channel channel;
    private String consumerTag;

    private static String EXCHANGE_NAME = "aurion.chat";

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

    public void join(String queueName) throws IOException{
        if(this.consumerTag != null){
            channel.basicCancel(consumerTag);
        }
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare(queueName, false, false, false, null);
        //canaux peuvent etre : aurion.chat.<server> et/ou aurion.automessage.<server>
        channel.queueBind(queueName, EXCHANGE_NAME, "aurion.#");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
          String message = new JsonParser().parse(json).getAsJsonObject().get("message").getAsString();
          String type = delivery.getEnvelope().getRoutingKey().split("\\.")[1];
          String channel = delivery.getEnvelope().getRoutingKey().split("\\.")[2].toLowerCase();
          if(type.equalsIgnoreCase("automessage")){
              sendAutoMessage(channel, message);
          } else if(type.equalsIgnoreCase("chat")) {
              sendMessage(channel, message);
          } else {
              System.out.println("Received message with the type " + type + " and the message was " + message + ". It won't be processed");
          }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void leave(String queueName) throws IOException {
        channel.queueDelete(queueName);
    }

    public void send(String channelName,String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        channel.basicPublish(EXCHANGE_NAME,"aurion.chat." + channelName, null, json.toString().getBytes());
    }
    public void close() throws TimeoutException, IOException{
        channel.close();
        connection.close();
    }

    abstract public void sendMessage(String channelName, String message);
    abstract public void sendAutoMessage(String channelName, String message);
    abstract public void desactivatePlugin();

}
