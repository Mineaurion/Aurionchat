package com.mineaurion.aurionchat.forge;

import net.kyori.adventure.text.Component;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

import static com.mineaurion.aurionchat.forge.AurionChat.toNativeText;

public class PlayerFactory extends com.mineaurion.aurionchat.common.player.PlayerFactory<ServerPlayer> {

    public PlayerFactory(){
        super(true);
    }

    @Override
    protected UUID getUUID(ServerPlayer player) {
        return player.getUUID();
    }

    @Override
    protected String getName(ServerPlayer player) {
        return player.getDisplayName().getString();
    }

    @Override
    protected void sendMessage(ServerPlayer player, Component message) {
        player.sendMessage(toNativeText(message), Util.NIL_UUID);
    }

    @Override
    protected boolean hasPermission(ServerPlayer player, String permission) {
        return AurionChat.luckPermsUtils.hasPermission(player.getUUID(), permission);
    }
}
