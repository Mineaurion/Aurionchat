package com.mineaurion.aurionchat.fabric.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.fabric.AurionChat;
import com.mineaurion.aurionchat.fabric.AurionChatPlayer;
import net.minecraft.server.network.ServerPlayerEntity;

public class LoginListener extends LoginListenerCommon<AurionChatPlayer> {

    public LoginListener(AurionChat plugin){
        super(plugin.getAurionChatPlayers());
    }

    public void onPlayerJoin(ServerPlayerEntity player){
        aurionChatPlayers.putIfAbsent(
                player.getUuid(),
                new AurionChatPlayer(player, AurionChat.config.channels.keySet())
        );
    }

    public void onPlayerQuit(ServerPlayerEntity player){
        this.playerLeaving(player.getUuid());
    }
}
