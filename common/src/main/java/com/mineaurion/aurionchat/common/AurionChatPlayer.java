package com.mineaurion.aurionchat.common;

import java.util.Set;

public class AurionChatPlayer {
    private Set<String> channels;
    private String currentChannel;

    public AurionChatPlayer(Set<String> channels, String currentChannel){

        super();
        this.channels = channels;
        this.currentChannel = currentChannel;
    }

    public String getCurrentChannel(){
        return this.currentChannel;
    }

    public void setCurrentChannel(String channel){
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
        this.channels.remove(channel);
    }

    @Override
    public String toString()
    {
        return "currentChannel:" + this.currentChannel + ",channels:" + this.channels.toString();
    }

}
