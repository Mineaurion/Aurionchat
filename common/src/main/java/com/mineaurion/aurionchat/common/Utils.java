package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class Utils {


    public static <T extends AurionChatPlayerCommon<?>> Component processMessage(String format, Component message, T aurionChatPlayer){
        if(!aurionChatPlayer.isAllowedColors()){
            message = message.color(WHITE);
        }

        String[] formatSplit = format.split("\\{message\\}");

        Component beforeMessage = replaceToken(formatSplit[0], aurionChatPlayer);
        Component afterMessage = replaceToken(formatSplit.length == 2 ? formatSplit[1] : "", aurionChatPlayer);

        return beforeMessage.append(message).append(afterMessage);
    }

    private static Component replaceToken(String text, AurionChatPlayerCommon<?> aurionChatPlayer){
        return LegacyComponentSerializer.legacy('&').deserialize(
                text.replace("{prefix}", aurionChatPlayer.getPrefix())
                        .replace("{suffix}", aurionChatPlayer.getSuffix())
                        .replace("{display_name}", aurionChatPlayer.getDisplayName())
        );
    }
}