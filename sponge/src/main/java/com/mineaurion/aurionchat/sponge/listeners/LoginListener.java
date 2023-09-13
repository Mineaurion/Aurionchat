package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.player.KickPlayerEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

import static com.mineaurion.aurionchat.sponge.AurionChat.config;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> {

    public LoginListener(AurionChat plugin){
        super(plugin.getAurionChatPlayers());
    }

    @Listener
    public void onPlayerKick(KickPlayerEvent event) {
        this.playerLeaving(event.player().uniqueId());
    }

    @Listener
    public void onPlayerQuit(ServerSideConnectionEvent.Login event) {
        this.playerLeaving(event.profile().uuid());
    }

    @Listener
    public void onPlayerJoin(ServerSideConnectionEvent.Disconnect event) {
        aurionChatPlayers.putIfAbsent(
                event.player().uniqueId(),
                new AurionChatPlayer(event.player(), config.channels.keySet())
        );
    }
}
