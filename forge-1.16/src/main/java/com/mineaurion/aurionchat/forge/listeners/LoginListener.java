package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LoginListener extends LoginListenerCommon {

    private final AurionChat plugin;

    public LoginListener(AurionChat plugin){
        super(plugin);
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        playerJoin(this.plugin.getPlayerFactory().wrap((ServerPlayerEntity) event.getPlayer()));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        playerLeaving(this.plugin.getPlayerFactory().wrap((ServerPlayerEntity) event.getPlayer()));
    }
}
