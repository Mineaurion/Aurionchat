package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Map;
import java.util.UUID;

import static com.mineaurion.aurionchat.sponge.AurionChat.config;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> {

    private static final Map<UUID, AurionChatPlayer> aurionChatPlayers = AurionChat.aurionChatPlayers;
    public LoginListener(){
        super(aurionChatPlayers);
    }

    @Listener
    public void onPlayerKick(KickPlayerEvent event) {
        this.playerLeaving(event.getTargetEntity().getUniqueId());
    }

    @Listener
    public void onPlayerQuit(ClientConnectionEvent.Disconnect event) {
        this.playerLeaving(event.getTargetEntity().getUniqueId());
    }

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        aurionChatPlayers.putIfAbsent(
                event.getTargetEntity().getUniqueId(),
                new AurionChatPlayer(event.getTargetEntity(), config.channels.keySet())
        );
    }
}
