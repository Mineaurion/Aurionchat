package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LoginListener {

    private AurionChat plugin;

    public LoginListener(AurionChat plugin){
        this.plugin = plugin;
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
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(event.getTargetEntity());
        if(aurionChatPlayer == null){
            Player player = event.getTargetEntity();
            UUID uuid = event.getTargetEntity().getUniqueId();
            String name = event.getTargetEntity().getName();
            Set<String> listening = new HashSet<>();
            String current = "global";
            listening.add(current);
            aurionChatPlayer = new AurionChatPlayer(uuid, name, current, listening, name, false);
            AurionChat.players.add(aurionChatPlayer);
            aurionChatPlayer.addListening("global");
            aurionChatPlayer.setCurrentChannel("global");
        }

    }

    private void playerLeaving(Player player){
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(player);
        if(aurionChatPlayer.getPlayer() == null){
            return;
        }
        aurionChatPlayer.setOnline(false);
        AurionChat.onlinePlayers.remove(aurionChatPlayer);
    }
}
