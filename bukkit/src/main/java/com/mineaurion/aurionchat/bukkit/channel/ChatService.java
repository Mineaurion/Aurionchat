package com.mineaurion.aurionchat.bukkit.channel;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ChatService {
    static final String FANOUT = "fanout";
    static final String EX_PREFIX = "bcast";
    Connection connection;
    Channel channel;
    Consumer consumer;
    private String consumerTag;
    private List<String> messageBuffer;
    private String CHANNEL;

    List<Map<String, String>> channelMember = new ArrayList<>();

    Map<String, String[]> map = new HashMap<String, String[]>();

    private AurionChat plugin;


    public ChatService(String host, AurionChat main) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
        this.messageBuffer = new ArrayList<>();
        this.plugin = main;
        this.CHANNEL = AurionChat.CHANNEL;
    }

    public void join(String nickname) throws IOException{
        if(this.consumerTag != null){
            channel.basicCancel(consumerTag);
        }
        channel.queueDeclare(getQueueName(nickname),false,false,false,null);
        channel.exchangeDeclare("channel_" + CHANNEL, FANOUT);
        channel.queueBind(getQueueName(nickname), "channel_" + CHANNEL, "");

        boolean autoAck = false;
        Consumer consumer = new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
              String routingKey = envelope.getRoutingKey();
              String contentType = properties.getContentType();
              long deliveryTag = envelope.getDeliveryTag();

              String message = new String(body, "UTF-8");
              String channelName = getChannelName(message);

              plugin.sendMessageToPlayer(channelName,message.replace(channelName + " ",""));
              Bukkit.getConsoleSender().sendMessage(message);

              channel.basicAck(deliveryTag, false);
          }
        };
        channel.basicConsume(getQueueName(nickname),autoAck, "myConsumerTag", consumer);
    }

    public void leave(String nickname) throws IOException {
        //E1
        channel.exchangeUnbind("channel_" + CHANNEL,"","");
        //E2
        channel.queueUnbind(getQueueName(nickname),"","");
    }

    public void send(String channelName,String message) throws IOException {
        //E2
        message = channelName + " " + message ;
        channel.basicPublish("channel_" + CHANNEL,"",null,message.getBytes());
    }

    private String getQueueName(String nickname){
        return "queue_" + nickname;
    }

    public List<String> takeMessages(){
        List<String> ret;
        synchronized (this){
            ret = new ArrayList<>(messageBuffer);
            messageBuffer.clear();
        }
        return ret;
    }

    public String getChannelName(String message){
        String[] split = message.split(" ");
        Bukkit.getConsoleSender().sendMessage(split[0]);
        return split[0];
    }


}
