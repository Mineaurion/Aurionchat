package com.mineaurion.aurionchat.sponge.channel;

import com.mineaurion.aurionchat.common.channel.ChatServiceCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatService extends ChatServiceCommon {
    private final Config config;

    public ChatService(String uri, String queueName) throws IOException, TimeoutException {
        super(uri, queueName);
        this.config = AurionChat.config;
    }

    @Override
    public void sendMessage(String channel, String message){
        AurionChat.utils.sendMessageToPlayer(channel, message);
        if(config.options.spy){
            Sponge.getGame().getServer().getConsole().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
        }
    }

    @Override
    public void sendAutoMessage(String channel, String message) {
        if(config.options.automessage){
            AurionChat.utils.broadcastToPlayer(channel, message);
        }
    }
}
