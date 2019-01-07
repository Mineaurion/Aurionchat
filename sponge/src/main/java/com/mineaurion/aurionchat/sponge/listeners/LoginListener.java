package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LoginListener {

    private AurionChat plugin;
    private Config config;
    private AurionChatPlayers aurionChatPlayers;

    public LoginListener(AurionChat plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
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
        UUID uuid = player.getUniqueId();
        Set<String> listenChannel = new HashSet<String>();
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
        aurionChatPlayers.addPlayer(uuid, listenChannel, currenChannel);
    }

    private void playerLeaving(Player player){
        aurionChatPlayers.removePlayer(player.getUniqueId());
    }
}
