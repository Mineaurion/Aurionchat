package com.mineaurion.aurionchat.forge;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.kyori.adventure.text.Component;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;

import java.util.UUID;

import static com.mineaurion.aurionchat.forge.AurionChat.toNativeText;

public class PlayerFactory extends com.mineaurion.aurionchat.common.player.PlayerFactory<ServerPlayerEntity> {

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

    @Override
    protected boolean hasPermission(ServerPlayerEntity player, String permission) {
        return FTBRanksAPI.getPermissionValue(player, permission).asBooleanOrFalse();
    }

    /**
     * The function keep the left part of the format and remove the right.
     * For example this config in ftbranks (some stuff of the config are omitted) :
     * member: {
     * 		ftbranks.name_format: "<{name}>"
     *        }
     * 	vip: {
     * 		ftbranks.name_format: "<&bVIP {name}&r>"
     *    }
     *  admin: {
     * 		ftbranks.name_format: "&4[Administrateur] {name}&r :"
     *  }
     *  The function will return this for :
     *    member: nothing
     *    vip:  "<&bVIP"
     *    admin: "&4[Administrateur]"
     */
    @Override
    public String getPreffix(ServerPlayerEntity player) {
        String name_format = FTBRanksAPI.getPermissionValue(player, "ftbranks.name_format").asString().orElse("");
        String[] split = name_format.split(" ");
        return split.length > 1 ? split[0] + " " : "";
    }

    @Override
    public String getSuffix(ServerPlayerEntity player) {
        return "";
    }
}
