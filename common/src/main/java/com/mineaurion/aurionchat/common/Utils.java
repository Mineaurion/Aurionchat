package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class Utils {
    public static Component processMessage(String format, Component message, AurionChatPlayer aurionChatPlayer){
        if(!aurionChatPlayer.isAllowedColors()){
            message = message.color(WHITE);
        }

        String[] formatSplit = format.split("\\{message\\}");

        Component beforeMessage = replaceToken(formatSplit[0], aurionChatPlayer);
        Component afterMessage = replaceToken(formatSplit.length == 2 ? formatSplit[1] : "", aurionChatPlayer);

        return beforeMessage.append(message).append(afterMessage);
    }

    private static Component replaceToken(String text, AurionChatPlayer aurionChatPlayer){
        return LegacyComponentSerializer.legacy('&').deserialize(
                text.replace("{prefix}", aurionChatPlayer.getPlayer().getPreffix())
                        .replace("{suffix}", aurionChatPlayer.getPlayer().getSuffix())
                        .replace("{display_name}", aurionChatPlayer.getPlayer().getDisplayName())
        );
    }
}