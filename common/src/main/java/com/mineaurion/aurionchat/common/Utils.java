package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class Utils {
    public static Component processMessage(String format, Component message, AurionChatPlayer aurionChatPlayer){
        if(!aurionChatPlayer.isAllowedColors()){
            Component messageWithoutStyle = Component.text("");
            if(!message.children().isEmpty()){
                for (Component component: message.children()) {
                    messageWithoutStyle = messageWithoutStyle.append(removeAllStyleAndColor(component));
                }
            } else {
                messageWithoutStyle = messageWithoutStyle.append(removeAllStyleAndColor(message));
            }
            message = messageWithoutStyle;
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

    private static Component removeAllStyleAndColor(Component component){
        return component
                .style(Style.empty())
                .decorations(
                        new HashSet<>(Arrays.asList(
                                TextDecoration.BOLD,
                                TextDecoration.ITALIC,
                                TextDecoration.OBFUSCATED,
                                TextDecoration.STRIKETHROUGH,
                                TextDecoration.UNDERLINED)
                        ),
                        false
                );
    }
}