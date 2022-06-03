package com.mineaurion.aurionchat.common.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.UtilsCommon;
import com.mineaurion.aurionchat.common.channel.ChatServiceCommon;

import java.io.IOException;

public class CommandListenerCommon<T extends AurionChatPlayerCommon<?>, T2 extends UtilsCommon<T>, T3 extends ChatServiceCommon> {

    private final T2 utils;
    private final T3 chatService;

    public CommandListenerCommon(T2 utils, T3 chatService){
        this.utils = utils;
        this.chatService = chatService;
    }

    public void onCommand(T aurionChatPlayers, String message, String channel, String format){
        if(message.length() == 0){
            aurionChatPlayers.sendMessage("&4Invalid command : /" + channel + " <message>");
        } else {
            aurionChatPlayers.addChannel(channel);
            String messageFormat = this.utils.processMessage(format, message, aurionChatPlayers);
            try {
                this.chatService.send(channel, messageFormat);
            } catch (IOException e){
                aurionChatPlayers.sendMessage("&4The server returned an error, your message could not be sent");
                System.err.println(e.getMessage());
            }
        }
    }
}
