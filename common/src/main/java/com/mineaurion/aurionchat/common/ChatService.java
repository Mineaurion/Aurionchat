package com.mineaurion.aurionchat.common;

import com.google.gson.Gson;
import com.mineaurion.aurionchat.api.RabbitMQMessage;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.impl.ForgivingExceptionHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class ChatService {
    private static final String EXCHANGE_NAME = "aurion.chat";

    private Connection connection;
    private Channel channel;
    protected boolean connected = false;

    private final AbstractAurionChat plugin;
    private final ConfigurationAdapter config;


    public ChatService(AbstractAurionChat plugin) throws IOException {
        this.plugin = plugin;
        this.config = this.plugin.getConfigurationAdapter();
        this.createConnection();
    }

    public String getUri() {
        return config.getString("rabbitmq.uri", "amqp://guest:guest@localhost:5672/");
    }

    private void createConnection() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        try{
            factory.setUri(getUri());
            factory.setAutomaticRecoveryEnabled(true);
            factory.setTopologyRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(10000);
            factory.setRequestedHeartbeat(10);
            factory.setExceptionHandler(new ForgivingExceptionHandler());

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
        this.createConnection();
    }

    private void join() throws IOException{
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");

        channel.basicConsume(queue, true, consumer(), consumerTag -> {});
    }

    private DeliverCallback consumer() {
        return (consumerTag, delivery) -> {
            RabbitMQMessage rabbitMQMessage = new Gson().fromJson(new String(delivery.getBody(), StandardCharsets.UTF_8), RabbitMQMessage.class);
            Component messageDeserialize = GsonComponentSerializer.gson().deserialize(rabbitMQMessage.getMessage());
            if (this.config.getBoolean("options.spy", false)) {
                plugin.getlogger().info(Utils.getDisplayString(messageDeserialize));
            }

            String channelName = rabbitMQMessage.getChannel();
            if (rabbitMQMessage.getType().equals(RabbitMQMessage.Type.AUTO_MESSAGE) && this.config.getBoolean("options.automessage", false)) {
                deliver(channelName, messageDeserialize, acp -> acp.hasPermission("aurionchat.automessage." + channelName));
            } else if (rabbitMQMessage.getType().equals(RabbitMQMessage.Type.CHAT)) {
                deliver(channelName, messageDeserialize);
            } else {
                plugin.getlogger().warn("Received message with the type " + rabbitMQMessage.getType() + " and the message was " + rabbitMQMessage + ". It won't be processed");
            }
        };
    }

    public void deliver(String channelName, Component message) {
        deliver(channelName, message, null);
    }

    public void deliver(String channelName, Component message, @Nullable Predicate<AurionChatPlayer> deliverToPlayer) {
        plugin.getAurionChatPlayers().forEach((uuid, acp) -> {
            if (acp.getChannels().contains(channelName) && (deliverToPlayer == null || deliverToPlayer.test(acp))) {
                acp.sendMessage(message);
            }
        });
    }

    public void send(String channelName, Component message) throws IOException {
        RabbitMQMessage rabbitMQMessage = new RabbitMQMessage(
                channelName,
                RabbitMQMessage.Type.CHAT,
                GsonComponentSerializer.gson().serialize(message)
        );

        channel.basicPublish(EXCHANGE_NAME, "", null, new Gson().toJson(rabbitMQMessage).getBytes());
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
