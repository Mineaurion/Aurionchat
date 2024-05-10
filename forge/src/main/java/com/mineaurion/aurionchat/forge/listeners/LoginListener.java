package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class LoginListener extends LoginListenerCommon<AurionChat> {

    public LoginListener(AurionChat plugin){
        super(plugin);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        playerJoin(plugin.getSenderFactory().wrap(event.getEntity().createCommandSourceStack()));
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        playerLeaving(plugin.getSenderFactory().wrap(event.getEntity().createCommandSourceStack()));
    }
}
