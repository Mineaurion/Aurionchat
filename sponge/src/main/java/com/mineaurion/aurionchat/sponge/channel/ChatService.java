package com.mineaurion.aurionchat.sponge.channel;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import com.mineaurion.aurionchat.sponge.Utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.mineaurion.aurionchat.sponge.Utils.sendConsoleMessage;

public class ChatService extends com.mineaurion.aurionchat.common.channel.ChatService {
    private final Config config;

    public ChatService(String uri, String queueName) throws IOException, TimeoutException {
        super(uri, queueName);
        this.config = AurionChat.config;
    }

    @Override
    public void sendMessage(String channel, String message){
        Utils.sendMessageToPlayer(channel, message);
        if(config.options.spy){
           sendConsoleMessage(message);
        }
    }

    @Override
    public void sendAutoMessage(String channel, String message) {
        if(config.options.automessage){
            Utils.broadcastToPlayer(channel, message);
        }
    }
}
