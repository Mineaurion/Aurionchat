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

    public Set<String> getChannels(){
        return channels;
    }

    public String getCurrentChannel(){
        return currentChannel;
    }

    public void setCurrentChannel(String channel){
        currentChannel = channel;
    }

    public void addListening(String channel){
        channels.add(channel);
    }

    public void removeListening(String channel){
        channels.remove(channel);
    }

}
