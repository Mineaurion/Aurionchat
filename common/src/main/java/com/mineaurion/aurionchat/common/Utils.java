package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.intellij.lang.annotations.MagicConstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    private static final Pattern URL_PATTERN = Pattern.compile("(?xi) # comments and case insensitive \n" +
            "(.*?)( # prepend least possible of anything and group actual result \n" +
            "(?<protocol>https?://)? # http/s \n" +
            "(?<domain>([\\w-]+\\.)*([\\w-]+(\\.[\\w-]+)+?)) # domains with * subdomains and +? endings \n" +
            "(?<path>(/[\\w-_.]+)*/?) # url path \n" +
            "(\\#(?<fragment>[\\w_-]+))? # url fragment \n" +
            "(?<parameters>\\?(([\\w_%-]+)=([\\w_%-]+)&?))? # urlencoded parameters \n" +
            ").*? # append anything");
    public static final int URL_MODE_ALLOW = 0x1;
    public static final int URL_MODE_ALLOW_HTTP = 0x2;
    public static final int URL_MODE_SCAN_DOMAINS = 0x4;
    public static final int URL_MODE_DISPLAY_ONLY_DOMAINS = 0x8;

    public static Component processMessage(String format, Component message, AurionChatPlayer aurionChatPlayer,
                                           @MagicConstant(flagsFromClass = Utils.class) int urlMode) {
        if (!aurionChatPlayer.isAllowedColors()) {
            Component messageWithoutStyle = Component.text("");
            if (!message.children().isEmpty()) {
                for (Component component : message.children()) {
                    messageWithoutStyle = messageWithoutStyle.append(removeAllStyleAndColor(component));
                }
            } else {
                messageWithoutStyle = messageWithoutStyle.append(removeAllStyleAndColor(message));
            }
            message = messageWithoutStyle;
        }

        String[] formatSplit = format.split("\\{message}");

        Component blob = replaceToken(formatSplit[0], aurionChatPlayer);
        final String display = getDisplayString(message);
        final Matcher matcher = URL_PATTERN.matcher(display);
        int eIndex = display.length();
        while (matcher.find()) {
            eIndex = matcher.end();

            // append text before url
            blob = blob.append(Component.text(matcher.group(1)));

            String urlDisplay = matcher.group(2), urlAction;

            // check protocol present
            if (matcher.group("protocol") == null) {
                // validate that simple domains should be scanned
                if ((urlMode & URL_MODE_SCAN_DOMAINS) == 0) {
                    // otherwise append url as plaintext and continue
                    blob = blob.append(Component.text(urlDisplay));
                    continue;
                }
                urlAction = "https://" + urlDisplay;
            }
            // check enforce https
            else if ((urlMode & URL_MODE_ALLOW_HTTP) == 0 && urlDisplay.startsWith("http:"))
                urlAction = urlDisplay = urlDisplay.replace("http:", "https:");
                // or just use the url
            else urlAction = urlDisplay;

            // check display mode
            if ((urlMode & URL_MODE_DISPLAY_ONLY_DOMAINS) != 0)
                urlDisplay = matcher.group("domain");

            // check urls allowed
            if ((urlMode & URL_MODE_ALLOW) != 0)
                blob = blob.append(Component.text(urlDisplay)
                        .clickEvent(ClickEvent.openUrl(urlAction)));
            else blob = blob.append(Component.text("[url removed]"));
        }
        return blob.append(Component.text(display.substring(eIndex)))
                .append(replaceToken(formatSplit.length == 2 ? formatSplit[1] : "", aurionChatPlayer));
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

    private static Component replaceToken(String text, AurionChatPlayer aurionChatPlayer) {
        return LegacyComponentSerializer.legacy('&').deserialize(
                text.replace("{prefix}", aurionChatPlayer.getPlayer().getPreffix())
                        .replace("{suffix}", aurionChatPlayer.getPlayer().getSuffix())
                        .replace("{display_name}", aurionChatPlayer.getPlayer().getDisplayName())
        );
    }

    private static Component removeAllStyleAndColor(Component component) {
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