package com.mineaurion.aurionchat;

import java.util.Set;

public class AurionChatPlayer
{
    private Set<String> channels;
    private String currentChannel;

    public AurionChatPlayer(Set<String> channels, String currentChannel)
    {
        this.setChannels(channels);
        this.setCurrentChannel(currentChannel);
    }

    public AurionChatPlayer(String currentChannel){
        this.setCurrentChannel(currentChannel);
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public Set<String> getChannels() {
        return channels;
    }

    public void setChannels(Set<String> channels) {
        this.channels = channels;
    }

    public void addChannel(String channel)
    {
        this.channels.add(channel);
    }

    public void removeChannel(String channel)
    {
        this.channels.remove(channel);
    }

    @Override
    public String toString()
    {
        return "currentChannel:" + this.currentChannel + ",channels:" + this.channels.toString();
    }
}
