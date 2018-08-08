package com.mineaurion.aurionchat.bukkit.channel;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ChatService {
    static final String FANOUT = "fanout";
    static final String EX_PREFIX = "bcast";
    Connection connection;
    Channel channel;
    Consumer consumer;
    String consumerTag;
    List<String> messageBuffer;

    public ChatService(String host) throws IOException, TimeoutException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
        this.messageBuffer = new ArrayList<>();
    }

    public void join(String nickname, String channelName) throws IOException{
        if(this.consumerTag != null){
            channel.basicCancel(consumerTag);
        }
        //
        channel.queueDeclare(getQueueName(nickname),false,false,false,null);
        //
        channel.exchangeDeclare(getChannelName(channelName), FANOUT);
        channel.queueBind(getQueueName(nickname), getChannelName(channelName), "");
        
        boolean autoAck = false;
        Consumer consumer = new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
              String routingKey = envelope.getRoutingKey();
              String contentType = properties.getContentType();
              long deliveryTag = envelope.getDeliveryTag();

              String message = new String(body, "UTF-8");
              Bukkit.getConsoleSender().sendMessage(message);

              channel.basicAck(deliveryTag, false);
          }
        };
        channel.basicConsume(getQueueName(nickname),autoAck, "myConsumerTag", consumer);
    }

    public void leave(String nickname,String channelName) throws IOException {
        //E1
        channel.exchangeUnbind(getChannelName(channelName),"","");
        //E2
        channel.queueUnbind(getQueueName(nickname),"","");
    }

    public void send(String channelName,String nickname,String message) throws IOException {
        StringBuilder builder = new StringBuilder()
                .append("[")
                .append(nickname)
                .append("]")
                .append(" ").append(message);
        //E2
        channel.basicPublish(getChannelName(channelName),"",null,builder.toString().getBytes());
    }


    String getChannelName(String channelname){
        return "channel_" + channelname;
    }

    String getQueueName(String nickname){
        return "queue_" + nickname;
    }

    public Channel getChannel() {
        return channel;
    }

    public List<String> takeMessages(){
        List<String> ret;
        synchronized (this){
            ret = new ArrayList<>(messageBuffer);
            messageBuffer.clear();
        }
        return ret;
    }


}
