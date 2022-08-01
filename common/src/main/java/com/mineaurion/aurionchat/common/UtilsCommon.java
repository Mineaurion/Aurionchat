package com.mineaurion.aurionchat.common;

import java.util.Map;
import java.util.UUID;

public abstract class UtilsCommon<T extends AurionChatPlayerCommon<?>> {

    public Map<UUID, T> aurionChatPlayers;

    public UtilsCommon(Map<UUID, T> aurionChatPlayers){
        this.aurionChatPlayers = aurionChatPlayers;
    }

    public String processMessage(String format, String message, T aurionChatPlayer){
        // TODO: traitement si couleur autorise ou non à faire
        //if(aurionChatPlayer.hasPermission("aurionchat.chat.colors"))
        return format
                .replace("{prefix}", aurionChatPlayer.getPrefix())
                .replace("{suffix}", aurionChatPlayer.getSuffix())
                .replace("{display_name}", aurionChatPlayer.getDisplayName())
                .replace("{message}", message);
    }

    public void sendMessageToPlayer(String channelName, String message){
        if(this.aurionChatPlayers.size() > 0){
            this.aurionChatPlayers.forEach((uuid, aurionChatPlayer) -> {
                if(aurionChatPlayer.getChannels().contains(channelName)){
                    aurionChatPlayer.sendMessage(message);
                    if(message.contains("@" + aurionChatPlayer.getDisplayName())){
                        aurionChatPlayer.notifyPlayer();
                    }
                }
            });
        }
    }

    public void broadcastToPlayer(String channelName, String message){
        if(this.aurionChatPlayers.size() > 0){
            this.aurionChatPlayers.forEach((uuid, aurionChatPlayer) -> {
                if(aurionChatPlayer.hasPermission("aurionchat.automessage." + channelName)){
                    aurionChatPlayer.sendMessage(message);
                }
            });
        }
    }
}