package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<ServerPlayerEntity> {

    public AurionChatPlayer(ServerPlayerEntity player, Set<String> channels){
        super(player, AurionChat.config.rabbitmq.serverName.get(), channels);
    }
    @Override
    public boolean hasPermission(String permission) {
        return FTBRanksAPI.getPermissionValue(this.player, permission).asBooleanOrFalse();
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(new StringTextComponent(message.replace("&", "§")), Util.NIL_UUID);
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

    @Override
    public void notifyPlayer(){
        this.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 10, 1);
    }
}
