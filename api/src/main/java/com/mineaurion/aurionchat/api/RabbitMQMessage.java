package com.mineaurion.aurionchat.api;

@Deprecated
public class RabbitMQMessage {

    private String channel;

    private AurionPacket.Type type;

    private String message;

    public RabbitMQMessage(String channel, AurionPacket.Type type, String message){
        this.channel = channel;
        this.type = type;
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public RabbitMQMessage setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public AurionPacket.Type getType() {
        return type;
    }

    public RabbitMQMessage setType(AurionPacket.Type type) {
        this.type = type;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RabbitMQMessage setMessage(String message) {
        this.message = message;
        return this;
    }

}
