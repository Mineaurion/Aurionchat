package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import com.mineaurion.aurionchat.forge.AurionChatPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> {

    private static final Map<UUID, AurionChatPlayer> aurionChatPlayers = AurionChat.aurionChatPlayers;

    public LoginListener(){
        super(aurionChatPlayers);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        aurionChatPlayers.putIfAbsent(
                event.getPlayer().getUUID(),
                new AurionChatPlayer((ServerPlayer) event.getPlayer(), AurionChat.config.getChannels().keySet())
        );
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        this.playerLeaving(event.getPlayer().getUUID());
    }
}
