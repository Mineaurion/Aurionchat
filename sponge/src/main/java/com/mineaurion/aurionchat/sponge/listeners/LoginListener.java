package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.player.KickPlayerEvent;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class LoginListener extends LoginListenerCommon<AurionChat> {

    public LoginListener(AurionChat plugin){
        super(plugin);
    }

    @Listener
    public void onPlayerKick(KickPlayerEvent event) {
        playerLeaving(plugin.getSenderFactory().wrap(event.player()));
    }

    @Listener
    public void onPlayerQuit(ServerSideConnectionEvent.Login event) {
        Sponge.server().player(event.profile().uniqueId()).ifPresent(serverPlayer -> playerLeaving(plugin.getSenderFactory().wrap(serverPlayer)));
    }

    @Listener
    public void onPlayerJoin(ServerSideConnectionEvent.Disconnect event) {
        playerJoin(plugin.getSenderFactory().wrap(event.player()));
    }
}
