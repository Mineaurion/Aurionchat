package com.mineaurion.aurionchat.sponge.channel;

import com.google.inject.Inject;
import com.mineaurion.aurionchat.common.channel.ChatService;
import com.mineaurion.aurionchat.sponge.AurionChat;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatServiceSponge extends ChatService {
    private AurionChat plugin;

    @Inject
    private Logger logger;

    public ChatServiceSponge(String host, AurionChat plugin) throws IOException, TimeoutException {
        super(host);
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(String channelName, String message){
        logger.info(message);
    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }
}
