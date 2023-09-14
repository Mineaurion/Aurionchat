package com.mineaurion.aurionchat.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class ChatService {
    private static final String EXCHANGE_NAME = "aurion.chat";

    private Connection connection;
    private Channel channel;

    private final String uri;
    protected boolean connected = false;

    private AbstractAurionChat<?> plugin;

    private static ChatService INSTANCE = null;

    private final ConfigurationAdapter config;

    public static ChatService getInstance(){
        return INSTANCE;
    }

    public ChatService(AbstractAurionChat<?> plugin) throws IOException {
        this.plugin = plugin;
        this.config = this.plugin.getConfigurationAdapter();
        this.uri = this.config.getString("rabbitmq.uri", "amqp://guest:guest@localhost:5672/");
        this.createConnection(uri);
        INSTANCE = this;
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
        channel.basicConsume(queue, true, consumer(), consumerTag -> {});
    }

    private DeliverCallback consumer(){
        return (consumerTag, delivery) -> {
            JsonObject json = new JsonParser().parse(new String(delivery.getBody(), StandardCharsets.UTF_8)).getAsJsonObject();

            String channel = json.get("channel").getAsString();
            String message = json.get("message").getAsString();
            String type = json.get("type").getAsString();
            Component messageDeserialize = GsonComponentSerializer.gson().deserialize(message);

            plugin.getAurionChatPlayers().forEach((uuid, aurionChatPlayers) -> {
                if(type.equalsIgnoreCase("automessage") && this.config.getBoolean("options.automessage", false)){
                    if(aurionChatPlayers.hasPermission("aurionchat.automessage." + channel)){
                        aurionChatPlayers.sendMessage(messageDeserialize);
                    }
                } else if (type.equalsIgnoreCase("chat")) {
                    if(this.config.getBoolean("options.spy", false)){
                        plugin.getlogger().info(message);
                    }
                    if(aurionChatPlayers.getChannels().contains(channel)){
                        aurionChatPlayers.sendMessage(messageDeserialize);
                    }
                } else {
                    plugin.getlogger().warn("Received message with the type " + type + " and the message was " + message + ". It won't be processed");
                }
            });
        };
    }

    public void send(String channelName, Component message) throws IOException {
        String serializedMessage = GsonComponentSerializer.gson().serialize(message);

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
