package com.mineaurion.aurionchat.common.command;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.exception.ChannelNotFoundException;

import java.util.Set;

public class ChatCommandCommon<T extends AurionChatPlayerCommon<?>> {

    private final Set<String> channels;

    public enum Action {
        JOIN,
        LEAVE,
        SPY,
        ALLLISTEN,
        DEFAULT,
    }

    public ChatCommandCommon(Set<String> channels){
        this.channels = channels;
    }

    private void join(T aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.removeChannel(aurionChatPlayer.getCurrentChannel());
        aurionChatPlayer.setCurrentChannel(channel);
        aurionChatPlayer.sendMessage("&6You have joined the " + channel + " channel.");
    }

    private void leave(T aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.removeChannel(channel);
        aurionChatPlayer.sendMessage("&6You have leaved the " + channel + " channel.");
    }

    private void spy(T aurionChatPlayer, String channel) throws ChannelNotFoundException {
        this.checkChannelExist(channel);
        aurionChatPlayer.addChannel(channel);
        aurionChatPlayer.sendMessage("&6You have spy the " + channel + " channel.");
    }

    private void allListen(T aurionChatPlayer, Set<String> channels)
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
        if(this.channels.contains(channel)){
            throw new ChannelNotFoundException("&6This channel doesn't exist");
        }
    }

    private void defaultCommand(T aurionChatPlayer){
        StringBuilder message = new StringBuilder();
        StringBuilder channels = new StringBuilder();

        aurionChatPlayer.getChannels().forEach(channel -> {
            channels.append(channel).append(" ");
        });

        message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                .append("&7Spying on channels:&f ").append(channels);
        aurionChatPlayer.sendMessage(message.toString());
    }

    public boolean execute(T aurionChatPlayer, String channel, Action action) {
        try{
            switch (action){
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
                default:
                    this.defaultCommand(aurionChatPlayer);
            }
            return true;
        } catch (ChannelNotFoundException e){
            aurionChatPlayer.sendMessage(e.getMessage());
            return false;
        }
    }
}
