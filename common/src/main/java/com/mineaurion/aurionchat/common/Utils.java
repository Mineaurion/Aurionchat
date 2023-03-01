package com.mineaurion.aurionchat.common;

import java.util.Map;
import java.util.UUID;

public class Utils {


    public static <T extends AurionChatPlayerCommon<?>> String processMessage(String format, String message, T aurionChatPlayer){
        // TODO: need some condition if player is allowed to used color or not
        //if(aurionChatPlayer.hasPermission("aurionchat.chat.colors"))
        return format
                .replace("{prefix}", aurionChatPlayer.getPrefix())
                .replace("{suffix}", aurionChatPlayer.getSuffix())
                .replace("{display_name}", aurionChatPlayer.getDisplayName())
                .replace("{message}", message);
    }

    public static <T extends AurionChatPlayerCommon<?>> void sendMessageToPlayer(String channelName, String message, Map<UUID, T> aurionChatPlayers){
        if(aurionChatPlayers.size() > 0){
            aurionChatPlayers.forEach((uuid, aurionChatPlayer) -> {
                if(aurionChatPlayer.getChannels().contains(channelName)){
                    aurionChatPlayer.sendMessage(message);
                    if(message.contains("@" + aurionChatPlayer.getDisplayName())){
                        aurionChatPlayer.notifyPlayer();
                    }
                }
            });
        }
    }

    public static <T extends AurionChatPlayerCommon<?>> void broadcastToPlayer(String channelName, String message, Map<UUID, T> aurionChatPlayers){
        if(aurionChatPlayers.size() > 0){
            aurionChatPlayers.forEach((uuid, aurionChatPlayer) -> {
                if(aurionChatPlayer.hasPermission("aurionchat.automessage." + channelName)){
                    aurionChatPlayer.sendMessage(message);
                }
            });
        }
    }
}