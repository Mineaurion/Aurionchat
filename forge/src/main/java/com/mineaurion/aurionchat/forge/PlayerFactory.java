package com.mineaurion.aurionchat.forge;

import net.kyori.adventure.text.Component;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

import java.util.UUID;

import static com.mineaurion.aurionchat.forge.AurionChat.toNativeText;

public class PlayerFactory extends com.mineaurion.aurionchat.common.player.PlayerFactory<ServerPlayerEntity> {

    public PlayerFactory(){
        super(true);
    }

    @Override
    protected UUID getUUID(ServerPlayerEntity player) {
        return player.getUUID();
    }

    @Override
    protected String getName(ServerPlayerEntity player) {
        return player.getDisplayName().getString();
    }

    @Override
    protected void sendMessage(ServerPlayerEntity player, Component message) {
        player.sendMessage(toNativeText(message), Util.NIL_UUID);
    }
}
