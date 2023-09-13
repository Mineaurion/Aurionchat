package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.kyori.adventure.text.Component;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<ServerPlayerEntity> {

    public AurionChatPlayer(ServerPlayerEntity player, Set<String> channels){
        super(player, channels);
    }
    @Override
    public boolean hasPermission(String permission) {
        return FTBRanksAPI.getPermissionValue(this.player, permission).asBooleanOrFalse();
    }

    @Override
    public void sendMessage(Component message) {
        //TODO: need test for casting. If ok cast when needed
        this.player.sendMessage((ITextComponent) message, Util.NIL_UUID);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUUID();
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
    public String getPrefix() {
        String name_format = FTBRanksAPI.getPermissionValue(player, "ftbranks.name_format").asString().orElse("");
        String[] split = name_format.split(" ");
        return split.length > 1 ? split[0] + " " : "";
    }

    @Override
    public String getSuffix() {
        return "";
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName().getString();
    }

}
