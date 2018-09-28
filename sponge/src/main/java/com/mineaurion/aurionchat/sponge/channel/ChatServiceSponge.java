package com.mineaurion.aurionchat.sponge.channel;

import com.mineaurion.aurionchat.common.channel.ChatService;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import com.mineaurion.aurionchat.sponge.Utils;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class ChatServiceSponge extends ChatService {
    private AurionChat plugin;
    private Config config;
    private Utils utils;

    public ChatServiceSponge(String uri, AurionChat plugin) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        super(uri);
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.utils = plugin.getUtils();
    }

    @Override
    public void sendMessage(String channelName, String message){
        String messageClean = message.replace(channelName + " ", "");
        //#TODO a check
        if(config.getAutomessageEnable()){
            Set<String> automessageChannels = config.getAllAutomessageChannel();
            if(automessageChannels.contains(channelName)){
                utils.broadcastToPlayer(channelName, message);
            }
        }
        utils.sendMessageToPlayer(channelName, messageClean);
        if(config.getConsoleSpy().equalsIgnoreCase("true")){
            plugin.sendConsoleMessage(message);
        }
    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }
}
