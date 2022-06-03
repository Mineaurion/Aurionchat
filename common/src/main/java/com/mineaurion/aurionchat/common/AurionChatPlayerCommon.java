package com.mineaurion.aurionchat.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AurionChatPlayerCommon<T> {
    public final T player;
    private Set<String> channels = new HashSet<>();
    private String currentChannel;
    private static final String DEFAULT_CHANNEL = "global";  // Player default channel when he is speaking. We assume global is always defined in the config

    public AurionChatPlayerCommon(T player, String serverChannel, Set<String> channels){
        this.player = player;
        this.setCurrentChannel(serverChannel);
        this.setChannels(new HashSet<>(Arrays.asList(serverChannel, DEFAULT_CHANNEL)));
        for(String channel: channels){
            if(this.hasPermission("aurionchat.joinchannel." + channel)){
                this.addChannel(channel);
                this.setCurrentChannel(channel);
            } else if (this.hasPermission("aurionchat.listenchannel." + channel)) {
                this.addChannel(channel);
            }
        }
    }

    public T getPlayer(){
        return this.player;
    }

    public String getCurrentChannel(){
        return this.currentChannel;
    }

    public void setCurrentChannel(String channel){
        this.addChannel(channel); // When the current channel we ensure that we have the channel listen
        this.currentChannel = channel;
    }

    public Set<String> getChannels(){
        return channels;
    }

    public void setChannels(Set<String> channels)
    {
        this.channels = channels;
    }

    public void addChannel(String channel)
    {
        this.channels.add(channel);
    }

    public void removeChannel(String channel)
    {
        // If the channel leaving is the currento one will be the first of the list to avoid that we can't view our message
        if(this.getCurrentChannel().equalsIgnoreCase(channel)){
            this.setCurrentChannel(this.getChannels().iterator().next());
        }
        this.channels.remove(channel);
    }

    public abstract boolean hasPermission(String permission);
    public abstract void sendMessage(String message);

    public abstract UUID getUniqueId();

    public abstract String getPrefix();

    public abstract String getSuffix();

    public abstract String getDisplayName();

    public abstract void notifyPlayer();

    @Override
    public String toString()
    {
        return "currentChannel:" + this.currentChannel +
                ",channels:" + this.channels.toString() +
                ",uuid:" + this.getUniqueId() +
                ",prefix:" + this.getPrefix() +
                ",suffix:" + this.getSuffix() +
                ",displayName:" + this.getDisplayName() +
                ",playerClass:" + this.player.getClass().getName();
    }

}
