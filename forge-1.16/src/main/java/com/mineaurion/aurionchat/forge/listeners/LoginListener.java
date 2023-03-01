package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import com.mineaurion.aurionchat.forge.AurionChatPlayer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> {

    public LoginListener(AurionChat plugin){
        super(plugin.getAurionChatPlayers());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        aurionChatPlayers.putIfAbsent(
                event.getPlayer().getUUID(),
                new AurionChatPlayer((ServerPlayerEntity) event.getPlayer(), AurionChat.config.getChannels().keySet())
        );
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        this.playerLeaving(event.getPlayer().getUUID());
    }
}
