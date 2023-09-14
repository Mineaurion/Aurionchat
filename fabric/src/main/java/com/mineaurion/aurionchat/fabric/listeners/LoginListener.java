package com.mineaurion.aurionchat.fabric.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.fabric.AurionChat;
import com.mineaurion.aurionchat.fabric.AurionChatPlayer;
import net.minecraft.server.network.ServerPlayerEntity;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> {

    private final AurionChat plugin;

    public LoginListener(AurionChat plugin){
        super(plugin.getAurionChatPlayers());
        this.plugin = plugin;
    }

    public void onPlayerJoin(ServerPlayerEntity player){
        aurionChatPlayers.putIfAbsent(
                player.getUuid(),
                new AurionChatPlayer(player, plugin.getConfigurationAdapter().getChannels().keySet())
        );
    }

    public void onPlayerQuit(ServerPlayerEntity player){
        this.playerLeaving(player.getUuid());
    }
}
