package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class LoginListener extends LoginListenerCommon<AurionChat> {

    public LoginListener(AurionChat plugin){
        super(plugin);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        playerJoin(plugin.getPlayerFactory().wrap((ServerPlayer) event.getPlayer()));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        playerLeaving(plugin.getPlayerFactory().wrap((ServerPlayer) event.getPlayer()));
    }
}
