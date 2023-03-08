package com.mineaurion.aurionchat.common.command;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.exception.ChannelNotFoundException;

import java.io.IOException;
import java.util.Set;

public class ChatCommandCommon {

    private final Set<String> channels;

    public enum Action {
        JOIN,
        LEAVE,
        SPY,
        ALLLISTEN,
        RELOAD,
        DEFAULT
    }

    public ChatCommandCommon(Set<String> channels){
        this.channels = channels;
    }

    private void join(AurionChatPlayerCommon<?> aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.removeChannel(aurionChatPlayer.getCurrentChannel());
        aurionChatPlayer.setCurrentChannel(channel);
        aurionChatPlayer.sendMessage("&6You have joined the " + channel + " channel.");
    }

    private void leave(AurionChatPlayerCommon<?> aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.removeChannel(channel);
        aurionChatPlayer.sendMessage("&6You have leaved the " + channel + " channel.");
    }

    private void spy(AurionChatPlayerCommon<?> aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.addChannel(channel);
        aurionChatPlayer.sendMessage("&6You have spy the " + channel + " channel.");
    }

    private void allListen(AurionChatPlayerCommon<?> aurionChatPlayer, Set<String> channels)
    {
        for(String channel: channels){
            try {
                this.spy(aurionChatPlayer, channel);
            } catch (ChannelNotFoundException e) {
                System.out.println("Error when spying, the channel doesn't exist");
            }
        }
    }

    private void checkChannelExist(String channel) throws ChannelNotFoundException {
        if(!this.channels.contains(channel)){
            throw new ChannelNotFoundException("&6This channel doesn't exist");
        }
    }

    public void defaultCommand(AurionChatPlayerCommon<?> aurionChatPlayer){
        StringBuilder message = new StringBuilder();
        StringBuilder channels = new StringBuilder();

        aurionChatPlayer.getChannels().forEach(channel -> {
            channels.append(channel).append(" ");
        });

        message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                .append("&7Spying on channels:&f ").append(channels);
        aurionChatPlayer.sendMessage(message.toString());
    }

    public boolean execute(AurionChatPlayerCommon<?> aurionChatPlayer, String channel, Action action) {
        try {
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
                    this.allListen(aurionChatPlayer, this.channels);
                    break;
                case RELOAD:
                    if (aurionChatPlayer.hasPermission("aurionchat.reload")) {
                        ChatService.getInstance().reCreateConnection();
                        aurionChatPlayer.sendMessage("Reconnect successfull");
                        aurionChatPlayer.sendMessage("For now this command doesn't reload the config");
                    }
                    break;
                default:
                    this.defaultCommand(aurionChatPlayer);
            }
            return true;
        } catch (ChannelNotFoundException e){
            aurionChatPlayer.sendMessage(e.getMessage());
            return false;
        } catch (IOException exception){
            System.out.println(exception.getMessage());
            return false;
        }
    }

    public static boolean onCommand(AurionChatPlayerCommon<?> aurionChatPlayers, String message, String channel, String format){
        if(message.length() == 0){
            aurionChatPlayers.sendMessage("&4Invalid command : /" + channel + " <message>");
        } else {
            aurionChatPlayers.addChannel(channel);
            String messageFormat = Utils.processMessage(format, message, aurionChatPlayers);
            try {
                ChatService.getInstance().send(channel, messageFormat);
                return true;
            } catch (IOException e){
                aurionChatPlayers.sendMessage("&4The server returned an error, your message could not be sent");
                System.err.println(e.getMessage());
            }
        }
        return false;
    }
}
