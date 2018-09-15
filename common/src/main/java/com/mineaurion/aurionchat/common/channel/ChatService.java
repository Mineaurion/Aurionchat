package com.mineaurion.aurionchat.common.channel;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public abstract class ChatService {
    private static final String FANOUT = "fanout";
    private static final String EX_PREFIX = "bcast";
    private Connection connection;
    private Channel channel;
    private Consumer consumer;
    private String consumerTag;
    private List<String> messageBuffer;
    private String CHANNEL;

    public ChatService(String host) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        this.connection = factory.newConnection();
        this.channel = this.connection.createChannel();
        this.messageBuffer = new ArrayList<>();
        this.CHANNEL = getCHANNEL();
    }

    public void join(String serverName) throws IOException{
        if(this.consumerTag != null){
            channel.basicCancel(consumerTag);
        }
        channel.queueDeclare(getQueueName(serverName),false,false,false,null);
        channel.exchangeDeclare("channel_" + CHANNEL, FANOUT);
        channel.queueBind(getQueueName(serverName), "channel_" + CHANNEL, "");

        boolean autoAck = false;
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                String message = new String(body, "UTF-8");
                String channelName = getChannelName(message);

                sendMessage(channelName, message);
                channel.basicAck(deliveryTag, false);
            }
        };
        channel.basicConsume(getQueueName(serverName), autoAck, "myConsumerTag", consumer);
    }

    public void leave(String serverName) throws IOException {
        //E1
        channel.exchangeUnbind("channel_" + CHANNEL,"","");
        //E2
        channel.queueUnbind(getQueueName(serverName),"","");
    }

    public void send(String channelName,String message) throws IOException {
        //E2
        message = channelName + " " + message ;
        channel.basicPublish("channel_" + CHANNEL,"",null,message.getBytes());
    }
    public void close() throws TimeoutException, IOException{
        channel.close();
        connection.close();
    }

    private String getQueueName(String serverName){
        return "queue_" + serverName;
    }

    private String getChannelName(String message){
        String[] split = message.split(" ");
        return split[0];
    }

    abstract public void sendMessage(String channelName, String message);
    abstract public String getCHANNEL();


}
