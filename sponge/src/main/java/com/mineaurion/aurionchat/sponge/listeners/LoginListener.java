package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.*;

public class LoginListener {
    private final Config config;
    private final Map<UUID, AurionChatPlayer> aurionChatPlayers;

    public LoginListener(){
        this.config = AurionChat.config;
        this.aurionChatPlayers = AurionChat.aurionChatPlayers;
    }

    @Listener
    public void onPlayerKick(KickPlayerEvent event){
        playerLeaving(event.getTargetEntity());
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event){
        playerLeaving(event.getTargetEntity());
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event){
        Player player = event.getTargetEntity();
        Set<String> listenChannel = new HashSet<>(Arrays.asList("global", config.rabbitmq.servername));
        String currenChannel = "global";
        for(String channel: config.channels.keySet()){
            if(player.hasPermission("aurionchat.joinchannel." + channel)){
                listenChannel.add(channel);
                currenChannel = channel;
            }
            if(player.hasPermission("aurionchat.listenchannel." + channel)){
                listenChannel.add(channel);
            }
        }
        AurionChatPlayer aurionChatPlayer = new AurionChatPlayer(listenChannel, currenChannel);
        this.aurionChatPlayers.putIfAbsent(event.getTargetEntity().getUniqueId(), aurionChatPlayer);
    }

    private void playerLeaving(Player player){
        this.aurionChatPlayers.remove(player.getUniqueId());
    }
}
