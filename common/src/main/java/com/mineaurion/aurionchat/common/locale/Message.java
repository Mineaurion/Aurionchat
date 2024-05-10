package com.mineaurion.aurionchat.common.locale;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public interface Message {

    Component PREFIX_COMPONENT = text()
            .color(GRAY)
            .append(text('['))
            .append(text()
                    .decoration(BOLD, true)
                    .append(text(AurionChatPlugin.NAME, AQUA))
            )
            .append(text(']'))
            .build();

    static TextComponent prefixed(ComponentLike component){
        return text()
                .append(PREFIX_COMPONENT)
                .append(space())
                .append(component)
                .build();
    }
    Args0 COMMAND_NO_PERMISSION = () -> prefixed(text("You do no have permission to use this command")).color(RED);

    Args0 PLAYER_NOT_REGISTERED = () -> prefixed(text("Your player is not registered on the server, there is an issue. Contact the administrator")).color(RED);

    Args1<String> CHANNEL_JOIN = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("You have joined the %s channel", channel), GOLD))
            )
    );

    Args1<String> CHANNEL_LEAVE = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("You have leave the %s channel", channel), GOLD))
            )
    );

    Args1<String> CHANNEL_SPY = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("You have spy the %s channel", channel), GOLD))
            )
    );

    Args2<String, String> COMMAND_DEFAULT = (currentChannel, channels) -> joinNewline(
            prefixed(
                    text("Your current channel:").color(GRAY).append(space()).append(text(currentChannel).color(WHITE))
            ),
            prefixed(
                    text("Spying on channel:").color(GRAY).append(space()).append(text(channels).color(WHITE))
            )
    );

    Args0 COMMAND_RELOAD = () -> joinNewline(
            prefixed(
                    text("Reconnect successfull").color(GREEN)
            ),
            prefixed(
                    text("For now this command doesn't reload the config").color(WHITE)
            )
    );

    Args0 COMMAND_RELOAD_ERROR = () -> prefixed(text("Can not reconnect check the server log for more information").color(RED));

    Args1<String> CHANNEL_NOT_FOUND = (channel) -> joinNewline(
            prefixed(text()
                    .color(AQUA)
                    .append(text(String.format("The channel %s was not found", channel), WHITE))
            )
    );

    Args1<String> REQUIRED_ARGUMENT = name -> text()
            .color(DARK_GRAY)
            .append(text('<'))
            .append(text(name, GRAY))
            .append(text('>'))
            .build();

    Args1<String> OPTIONAL_ARGUMENT = name -> text()
            .color(DARK_GRAY)
            .append(text('['))
            .append(text(name, GRAY))
            .append(text(']'))
            .build();


    static Component joinNewline(final ComponentLike... components) {
        return join(JoinConfiguration.newlines(), components);
    }

    interface Args0 {
        Component build();

        default void send(ServerPlayer sender) {
            sender.sendMessage(build());
        }
    }

    interface Args1<A0> {
        Component build(A0 arg0);

        default void send(ServerPlayer sender, A0 arg0) {
            sender.sendMessage(build(arg0));
        }
    }

    interface Args2<A0, A1> {
        Component build(A0 arg0, A1 arg1);

        default void send(ServerPlayer sender, A0 arg0, A1 arg1) {
            sender.sendMessage(build(arg0, arg1));
        }
    }
}