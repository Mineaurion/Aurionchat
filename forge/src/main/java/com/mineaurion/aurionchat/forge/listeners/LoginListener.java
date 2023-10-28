package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class LoginListener extends LoginListenerCommon<AurionChat> {

    public LoginListener(AurionChat plugin){
        super(plugin);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        playerJoin(plugin.getPlayerFactory().wrap((ServerPlayerEntity) event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        playerLeaving(plugin.getPlayerFactory().wrap((ServerPlayerEntity) event.getEntity()));
    }
}
