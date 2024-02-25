package com.mineaurion.aurionchat.fabric.listeners;

import com.mineaurion.aurionchat.common.listeners.PlayerListenerCommon;
import com.mineaurion.aurionchat.fabric.AurionChat;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerListener extends PlayerListenerCommon<AurionChat> {
    public PlayerListener(AurionChat plugin){
        super(plugin);
    }

    public void onPlayerJoin(ServerPlayerEntity player){
        playerJoin(plugin.getPlayerFactory().wrap(player));
    }

    public void onPlayerQuit(ServerPlayerEntity player){
        this.playerLeaving(plugin.getPlayerFactory().wrap(player));
    }
}
