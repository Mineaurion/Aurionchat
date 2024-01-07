package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static String getDisplayString(Component component) {
        return Stream.concat(Stream.of(component), component.children().stream())
                .map(it -> {
                    if (it instanceof TextComponent)
                        return ((TextComponent) it).content();
                    // todo: support other types
                    //else if (it instanceof BlockNBTComponent) ;
                    //else if (it instanceof EntityNBTComponent) ;
                    //else if (it instanceof KeybindComponent) ;
                    //else if (it instanceof ScoreComponent) ;
                    //else if (it instanceof SelectorComponent) ;
                    //else if (it instanceof StorageNBTComponent) ;
                    //else if (it instanceof TranslatableComponent) ;
                    return "";
                })
                .collect(Collectors.joining());
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