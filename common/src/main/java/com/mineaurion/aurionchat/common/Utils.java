package com.mineaurion.aurionchat.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern URL_PATTERN = Pattern.compile("(?xi) # comments and case insensitive \n" +
            "(.*?)( # prepend least possible of anything and group actual result \n" +
            "(?<protocol>https?://)? # http/s \n" +
            "(?<domain>([\\w-]+\\.)*([\\w-]+(\\.[\\w-]+)+?)) # domains with * subdomains and +? endings \n" +
            "(?<url>(?<path>(/[\\w-_.]+)*/?)\n(\\#(?<fragment>[\\w_-]+))?\n(?<parameters>\\?(([\\w_%-]+)=([\\w_%-]+)&?))? # all of the url \n" +
            ")).*? # append anything");

    public enum URL_MODE {
        DISALLOW,
        DISSALLOW_URL,
        DISPLAY_ONLY_DOMAINS,
        ALLOW,
        FORCE_HTTPS,
        CLICK_DOMAIN
    }

    public static Component processMessage(String format, Component message, AurionChatPlayer aurionChatPlayer, List<URL_MODE> urlModes) {
        Component processedMessage = urlScanning(message, urlModes);
        if (!aurionChatPlayer.isAllowedColors()) {
            Component messageWithoutStyle = Component.text("");
            if (!processedMessage.children().isEmpty()) {
                for (Component component : processedMessage.children()) {
                    messageWithoutStyle = messageWithoutStyle.append(removeAllStyleAndColor(component));
                }
            } else {
                messageWithoutStyle = messageWithoutStyle.append(removeAllStyleAndColor(processedMessage));
            }
            processedMessage = messageWithoutStyle;
        }

        String[] formatSplit = format.split("\\{message\\}");

        return Component.text()
                .append(replaceToken(formatSplit[0], aurionChatPlayer))
                .append(processedMessage.children())
                .append(replaceToken(formatSplit.length == 2 ? formatSplit[1] : "", aurionChatPlayer))
                .build()
                ;
    }

    /**
     * Sanitize url or add click event on it
     */
    private static Component urlScanning(Component message, List<URL_MODE> urlModes) {
        final Component urlRemoved = Component.text("[url removed]");
        TextComponent.Builder component = Component.text();

        if(!message.children().isEmpty()){
            message.children().forEach(children -> component.append(urlScanning(children, urlModes)));
        }

        if(message instanceof TextComponent){
            TextComponent.Builder builder = Component.text();

            final String display = ((TextComponent) message).content();
            final Matcher matcher = URL_PATTERN.matcher(display);

            if(matcher.find()){
                matcher.reset(); // Discard internal state
                int eIndex = display.length();
                while(matcher.find()){
                    eIndex = matcher.end();
                    // append text before url
                    builder.append(Component.text(matcher.group(1)));

                    if(urlModes.contains(URL_MODE.DISALLOW)){
                        builder.append(urlRemoved);
                    } else {
                        String urlDisplay = matcher.group(2);
                        String urlAction = "";

                        if(urlModes.contains(URL_MODE.FORCE_HTTPS) && urlDisplay.startsWith("http:")){
                            urlDisplay = urlDisplay.replace("http:", "https:");
                        }
                        if(matcher.group("protocol") != null  || !matcher.group("url").isEmpty() ){
                            if(urlModes.contains(URL_MODE.DISSALLOW_URL)){
                                builder.append(urlRemoved);
                                continue;
                            }
                        }
                        if(urlModes.contains(URL_MODE.DISPLAY_ONLY_DOMAINS)){
                            urlDisplay = matcher.group("domain");
                        }

                        if(urlModes.contains(URL_MODE.CLICK_DOMAIN) || urlModes.contains(URL_MODE.ALLOW)){
                            String protocol = urlDisplay.startsWith("http") ? "" : "https://";
                            urlAction = protocol + urlDisplay + matcher.group("url");
                        }

                        Component clickComponent = urlAction.isEmpty() ? Component.text(urlDisplay) : Component.text(urlDisplay)
                                .decorate(TextDecoration.UNDERLINED)
                                .clickEvent(ClickEvent.openUrl(urlAction));

                        builder.append(clickComponent);
                    }
                }
                // Append last non match string
                builder.append(Component.text(display.substring(eIndex)));
            } else {
                // Don't match anything then just append the string to the component
                builder.append(Component.text(display));
            }
            component.append(builder.build());
        }
        return component.build();
    }

    public static String getDisplayString(Component component) {
        StringBuilder content = new StringBuilder();

        if(!component.children().isEmpty()){
            component.children().forEach( child -> content.append(getDisplayString(child)));
        }

        if(component instanceof TextComponent){
            content.append(((TextComponent) component).content());
        }
        // todo: support other types
        //else if (it instanceof BlockNBTComponent) ;
        //else if (it instanceof EntityNBTComponent) ;
        //else if (it instanceof KeybindComponent) ;
        //else if (it instanceof ScoreComponent) ;
        //else if (it instanceof SelectorComponent) ;
        //else if (it instanceof StorageNBTComponent) ;
        //else if (it instanceof TranslatableComponent) ;
        return content.toString();
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