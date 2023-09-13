package com.mineaurion.aurionchat.common;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class ChatService {
    private static final String EXCHANGE_NAME = "aurion.chat";

    private Connection connection;
    private Channel channel;

    private final String uri;
    protected boolean connected = false;

    private final DeliverCallback consumer;

    private static ChatService INSTANCE = null;

    private final MiniMessage miniMessage;

    public static ChatService getInstance(){
        return INSTANCE;
    }

    public ChatService(String uri, DeliverCallback consumer) throws IOException {
        this.uri = uri;
        this.consumer = consumer;
        this.createConnection(uri);
        INSTANCE = this;
        miniMessage = MiniMessage.miniMessage();
    }

    private void createConnection(String uri) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        try{
            factory.setUri(uri);
            factory.setAutomaticRecoveryEnabled(true);
            factory.setTopologyRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(10000);
            factory.setRequestedHeartbeat(10);

            connection = factory.newConnection();
            channel = connection.createChannel();
            join();
            connected = true;
        } catch (KeyManagementException|URISyntaxException|NoSuchAlgorithmException UriKeyException){
            System.out.println("Uri Syntax Exception, please check the config or the documentation of rabbitmq");
            throw new IOException(UriKeyException);
        } catch (IOException|TimeoutException e) {
            System.out.println("Can't connect to rabbitmq, check the log for more error");
            System.out.println(e.getMessage());
            throw new IOException(e);
        }
    }

    public void reCreateConnection() throws IOException {
        this.close();
        this.createConnection(this.uri);
    }

    private void join() throws IOException{
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");
        channel.basicConsume(queue, true, consumer, consumerTag -> {});
    }

    public void send(String channelName, Component message) throws IOException {
        String serializedMessage = miniMessage.serialize(message);

        JsonObject json = new JsonObject();
        json.addProperty("channel", channelName);
        json.addProperty("type", "chat");
        json.addProperty("message", serializedMessage);

        channel.basicPublish(EXCHANGE_NAME, "", null, json.toString().getBytes());
    }
    public void close(){
        try {
            channel.close();
            connection.close();
        } catch (IOException|TimeoutException e){
            System.out.println("Error when communication closed");
            System.out.println(e.getMessage());
        }
    }
}
