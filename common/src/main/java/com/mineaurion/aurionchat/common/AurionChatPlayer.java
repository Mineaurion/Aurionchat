package com.mineaurion.aurionchat.common;

import com.mineaurion.aurionchat.api.AurionPlayer;
import com.mineaurion.aurionchat.api.model.Player;
import com.mineaurion.aurionchat.api.model.ServerPlayer;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AurionChatPlayer extends AurionPlayer {
    private final ServerPlayer player;
    private Set<String> channels = new HashSet<>();
    private String currentChannel;
    private static final String DEFAULT_CHANNEL = "global";  // Player default channel when he is speaking. We assume global is always defined in the config

    public AurionChatPlayer(ServerPlayer player, AbstractAurionChat plugin){
        super(player.getId(),
                player.getDisplayName(),
                player.getPrefix(),
                player.getSuffix());
        this.player = player;
        this.setCurrentChannel(DEFAULT_CHANNEL);
        this.setChannels(new HashSet<>(Collections.singletonList(DEFAULT_CHANNEL)));
        for(String channel: plugin.getConfigurationAdapter().getChannels().keySet()){
            if(player.hasPermission("aurionchat.joinchannel." + channel)){
                this.addChannel(channel);
                this.setCurrentChannel(channel);
            } else if (player.hasPermission("aurionchat.listenchannel." + channel)) {
                this.addChannel(channel);
            }
        }
    }

    public Player getPlayer(){
        return player;
    }

    public void sendMessage(Component message){
        this.player.sendMessage(message);
    }

    public boolean hasPermission(String permission){
        return this.player.hasPermission(permission);
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

    public boolean isAllowedColors(){
        return player.hasPermission("aurionchat.chat.colors");
    }

    public boolean canSpeak(){
        return player.hasPermission("aurionchat.chat.speak");
    }

    @Override
    public String toString()
    {
        return "currentChannel:" + this.currentChannel +
                ",channels:" + this.channels.toString() +
                ",uuid:" + this.player.getId() +
                ",prefix:" + this.player.getPrefix() +
                ",suffix:" + this.player.getSuffix() +
                ",name:" + this.player.getDisplayName()
                ;
    }

}
