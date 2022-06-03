package com.mineaurion.aurionchat.common.channel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public abstract class ChatServiceCommon {
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private Connection connection;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    private Channel channel;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    private String queueName;

    private static final String EXCHANGE_NAME = "aurion.chat";

    public ChatServiceCommon(String uri, String queueName) throws IOException, TimeoutException {
        this.setQueueName(queueName);
        ConnectionFactory factory = new ConnectionFactory();
        try{
            factory.setUri(uri);
        }
        catch (KeyManagementException|URISyntaxException|NoSuchAlgorithmException UriKeyException){
            System.out.println("Uri Syntax Exception, please check the config or the documentation of rabbitmq");
        }
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);

        this.setConnection(factory.newConnection());
        this.setChannel(this.getConnection().createChannel());
        this.join(this.getQueueName());
    }

    private void join(String queueName) throws IOException{
        channel = this.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare(queueName, false, false, false, null);
        //canaux peuvent etre : aurion.chat.<server> et/ou aurion.automessage.<server>
        channel.queueBind(queueName, EXCHANGE_NAME, "aurion.chat.*");
        channel.queueBind(queueName, EXCHANGE_NAME, "aurion.automessage.*");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
          String message = new JsonParser().parse(json).getAsJsonObject().get("message").getAsString();

          String[] routingKeySplit = delivery.getEnvelope().getRoutingKey().split("\\.");
          String type = routingKeySplit[1];
          String channel = routingKeySplit[2].toLowerCase();
          if(type.equalsIgnoreCase("automessage")){
              this.sendAutoMessage(channel, message);
          } else if(type.equalsIgnoreCase("chat")) {
              this.sendMessage(channel, message);
          } else {
              System.out.println("Received message with the type " + type + " and the message was " + message + ". It won't be processed");
          }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    public void send(String channelName,String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("message", message);

        channel.basicPublish(EXCHANGE_NAME,"aurion.chat." + channelName, null, json.toString().getBytes());
    }
    public void close() throws TimeoutException, IOException{
        channel = this.getChannel();
        channel.queueDelete(this.getQueueName());
        channel.close();
        this.getConnection().close();
    }

    abstract public void sendMessage(String channelName, String message);
    abstract public void sendAutoMessage(String channelName, String message);
}
