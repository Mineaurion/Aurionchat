package com.mineaurion.aurionchat.fabric;

import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

import static com.mineaurion.aurionchat.fabric.AurionChat.toNativeText;

public class PlayerFactory extends com.mineaurion.aurionchat.common.player.PlayerFactory<ServerPlayerEntity> {

    public PlayerFactory(){
        super(true);
    }

    @Override
    protected UUID getUUID(ServerPlayerEntity player) {
        return player.getUuid();
    }

    @Override
    protected String getName(ServerPlayerEntity player) {
        return player.getDisplayName().getString();
    }

    @Override
    protected void sendMessage(ServerPlayerEntity player, Component message) {
        player.sendMessage(toNativeText(message));
    }

    @Override
    protected boolean hasPermission(ServerPlayerEntity player, String permission) {
        return AurionChat.luckPermsUtils.hasPermission(player.getUuid(), permission);
    }
}
