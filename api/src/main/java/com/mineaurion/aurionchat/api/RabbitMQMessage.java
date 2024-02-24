package com.mineaurion.aurionchat.api;

import com.google.gson.annotations.SerializedName;

public class RabbitMQMessage {

    private String channel;

    public enum Type {
        @SerializedName("chat")
        CHAT,
        @SerializedName("automessage")
        AUTO_MESSAGE;
    };

    private Type type;

    private String message;

    public RabbitMQMessage(String channel, Type type, String message){
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

    public Type getType() {
        return type;
    }

    public RabbitMQMessage setType(Type type) {
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
