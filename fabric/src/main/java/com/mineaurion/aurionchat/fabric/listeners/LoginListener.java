package com.mineaurion.aurionchat.fabric.listeners;

import com.mineaurion.aurionchat.common.listeners.LoginListenerCommon;
import com.mineaurion.aurionchat.fabric.AurionChat;
import net.minecraft.server.network.ServerPlayerEntity;

public class LoginListener extends LoginListenerCommon {

    private final AurionChat plugin;

    public LoginListener(AurionChat plugin){
        super(plugin);
        this.plugin = plugin;
    }

    public void onPlayerJoin(ServerPlayerEntity player){
        playerJoin(plugin.getPlayerFactory().wrap(player));
    }

    public void onPlayerQuit(ServerPlayerEntity player){
        this.playerLeaving(plugin.getPlayerFactory().wrap(player));
    }
}
