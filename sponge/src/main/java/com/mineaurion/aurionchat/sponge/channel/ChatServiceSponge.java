package com.mineaurion.aurionchat.sponge.channel;

import com.mineaurion.aurionchat.common.channel.ChatService;
import com.mineaurion.aurionchat.sponge.AurionChat;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChatServiceSponge extends ChatService {
    private AurionChat plugin;

    public ChatServiceSponge(String host, AurionChat plugin) throws IOException, TimeoutException {
        super(host);
        this.plugin = plugin;
    }

    @Override
    public void sendMessage(String channelName, String message){
        plugin.getUtils().sendMessageToPlayer(channelName,message.replace(channelName + " ",""));
        if(plugin.getConfig().getConsoleSpy().equalsIgnoreCase("true")){
            plugin.sendConsoleMessage(message);
        }
    }

    @Override
    public String getCHANNEL(){
        return AurionChat.CHANNEL;
    }
}
