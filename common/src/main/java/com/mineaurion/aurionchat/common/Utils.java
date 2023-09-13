package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class Utils {


    public static <T extends AurionChatPlayerCommon<?>> Component processMessage(String format, Component message, T aurionChatPlayer){
        if(!aurionChatPlayer.isAllowedColors()){
            message = message.color(WHITE);
        }

        MiniMessage miniMessage = MiniMessage.miniMessage();
        Component prefixMessage = miniMessage.deserialize(format
                .replace("{prefix}", aurionChatPlayer.getPrefix())
                .replace("{suffix}", aurionChatPlayer.getSuffix())
                .replace("{display_name}", aurionChatPlayer.getDisplayName()));

        return prefixMessage.append(message);
    }

    public static <T extends AurionChatPlayerCommon<?>> void sendMessageToPlayer(String channelName, Component message, Map<UUID, T> aurionChatPlayers){
        if(!aurionChatPlayers.isEmpty()){
            aurionChatPlayers.forEach((uuid, aurionChatPlayer) -> {
                if(aurionChatPlayer.getChannels().contains(channelName)){
                    aurionChatPlayer.sendMessage(message);
                }
            });
        }
    }

    public static <T extends AurionChatPlayerCommon<?>> void broadcastToPlayer(String channelName, Component message, Map<UUID, T> aurionChatPlayers){
        if(!aurionChatPlayers.isEmpty()){
            aurionChatPlayers.forEach((uuid, aurionChatPlayer) -> {
                if(aurionChatPlayer.hasPermission("aurionchat.automessage." + channelName)){
                    aurionChatPlayer.sendMessage(message);
                }
            });
        }
    }
}