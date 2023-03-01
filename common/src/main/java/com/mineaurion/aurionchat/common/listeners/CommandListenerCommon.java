package com.mineaurion.aurionchat.common.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;

import java.io.IOException;

public class CommandListenerCommon<T extends AurionChatPlayerCommon<?>> {

    public void onCommand(T aurionChatPlayers, String message, String channel, String format){
        if(message.length() == 0){
            aurionChatPlayers.sendMessage("&4Invalid command : /" + channel + " <message>");
        } else {
            aurionChatPlayers.addChannel(channel);
            String messageFormat = Utils.processMessage(format, message, aurionChatPlayers);
            try {
                ChatService.getInstance().send(channel, messageFormat);
            } catch (IOException e){
                aurionChatPlayers.sendMessage("&4The server returned an error, your message could not be sent");
                System.err.println(e.getMessage());
            }
        }
    }
}
