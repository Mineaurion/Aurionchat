package com.mineaurion.aurionchat.sponge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.util.Tristate;

import java.util.UUID;

public class PlayerFactory extends com.mineaurion.aurionchat.common.player.PlayerFactory<ServerPlayer> {

    public PlayerFactory(){
        super(true);
    }

    @Override
    protected UUID getUUID(ServerPlayer player) {
        return player.uniqueId();
    }

    @Override
    protected String getName(ServerPlayer player) {
        return PlainTextComponentSerializer.plainText().serialize(player.displayName().get());
    }

    @Override
    protected void sendMessage(ServerPlayer player, Component message) {
        player.sendMessage(message);
    }

    @Override
    protected boolean hasPermission(ServerPlayer player, String permission, boolean explicitly) {
        Tristate tristate = player.permissionValue(permission);
        return explicitly ? tristate == Tristate.TRUE : tristate != Tristate.FALSE;
    }
}
