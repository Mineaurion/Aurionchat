package com.mineaurion.aurionchat.common.command;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.exception.ChannelNotFoundException;
import com.mineaurion.aurionchat.common.exception.InsufficientPermissionException;
import net.kyori.adventure.text.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class ChatCommandCommon {
    private final AbstractAurionChat plugin;

    public enum Action {
        JOIN,
        LEAVE,
        SPY,
        ALLLISTEN,
        RELOAD,
        DEFAULT
    }

    public ChatCommandCommon(AbstractAurionChat plugin){
        this.plugin = plugin;
    }

    public AbstractAurionChat getPlugin(){
        return plugin;
    }

    private void join(AurionChatPlayer aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.setCurrentChannel(channel);
        aurionChatPlayer.sendMessage(text("You have joined the " + channel + " channel.").color(GOLD));
    }

    private void leave(AurionChatPlayer aurionChatPlayer, String channel) throws ChannelNotFoundException {
        aurionChatPlayer.removeChannel(channel);
        aurionChatPlayer.sendMessage(text("You have leave the " + channel + " channel.").color(GOLD));
    }

    private void spy(AurionChatPlayer aurionChatPlayer, String channel) throws ChannelNotFoundException {
        aurionChatPlayer.addChannel(channel);
        aurionChatPlayer.sendMessage(text("You have spy the " + channel + " channel.").color(GOLD));
    }

    private void allListen(AurionChatPlayer aurionChatPlayer, Set<String> channels)
    {
        for(String channel: channels){
            try {
                this.spy(aurionChatPlayer, channel);
            } catch (ChannelNotFoundException e) {
                System.out.println("Error when spying, the channel doesn't exist");
            }
        }
    }

    public void defaultCommand(AurionChatPlayer aurionChatPlayer){
        StringBuilder channels = new StringBuilder();

        aurionChatPlayer.getChannels().forEach(channel -> {
            channels.append(channel).append(" ");
        });

        Component message = text("Your current channel:").color(GRAY)
                .append(space())
                .append(text(aurionChatPlayer.getCurrentChannel()).color(WHITE))
                .append(newline())
                .append(text("Spying on channel:").color(GRAY))
                .append(space())
                .append(text(channels.toString()).color(WHITE))
                ;

        aurionChatPlayer.sendMessage(message);
    }

    public boolean execute(AurionChatPlayer aurionChatPlayer, String channel, Action action) {
        try {
            if(!plugin.getConfigurationAdapter().getChannels().containsKey(channel)){
                throw new ChannelNotFoundException("&6This channel doesn't exist");
            }

            // TODO: need to check permission for completion, need something like AurionEconomy to have better handling
            String perm = "aurionchat.channel." + action.name().toLowerCase() + "." + channel;
            // action is any of [join, leave or spy]
            if(action.ordinal() <= 2 && !aurionChatPlayer.hasPermission(perm)){
                throw new InsufficientPermissionException((perm));
            }

            switch (action) {
                case JOIN:
                    this.join(aurionChatPlayer, channel);
                    break;
                case LEAVE:
                    this.leave(aurionChatPlayer, channel);
                    break;
                case SPY:
                    this.spy(aurionChatPlayer, channel);
                    break;
                case ALLLISTEN:
                    perm = "aurionchat.channel.alllisten";
                    if(!aurionChatPlayer.hasPermission(perm)){
                        throw new InsufficientPermissionException(perm);
                    }
                    this.allListen(aurionChatPlayer, plugin.getConfigurationAdapter().getChannels().keySet());
                    break;
                case RELOAD:
                    perm = "aurionchat.reload";
                    if (!aurionChatPlayer.hasPermission(perm)){
                        throw new InsufficientPermissionException(perm);
                    }

                    plugin.getChatService().reCreateConnection();
                    aurionChatPlayer.sendMessage(
                            text("Reconnect successfull").color(GREEN)
                            .append(newline())
                            .append(text("For now this command doesn't reload the config").color(WHITE))
                    );
                    break;
                default:
                    this.defaultCommand(aurionChatPlayer);
            }
            return true;
        } catch (ChannelNotFoundException e){
            aurionChatPlayer.sendMessage(text(e.getMessage()).color(RED));
            return false;
        } catch (InsufficientPermissionException e){
            aurionChatPlayer.sendMessage(text("You are missing the required permission: "+e.getMessage()).color(RED));
            return false;
        } catch (IOException exception){
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public boolean onCommand(AurionChatPlayer aurionChatPlayers, Component message, String channel, String format){
        aurionChatPlayers.addChannel(channel);
        Component messageFormat = Utils.processMessage(format, message, aurionChatPlayers, Collections.singletonList(Utils.URL_MODE.ALLOW), false);
        try {
            plugin.getChatService().send(channel, messageFormat);
            return true;
        } catch (IOException e){
            aurionChatPlayers.sendMessage(text("The server returned an error, your message could not be sent").color(DARK_RED));
            System.err.println(e.getMessage());
        }
        return false;
    }
}
