package com.mineaurion.aurionchat;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Utils {

    public static String processMessage(String channel, String message, ServerPlayerEntity player)
    {
        String channelFormat = AurionChat.config.getChannels().get(channel).format;
        // name format in FTB Ranks contains the full display of name and rank ex: <&2{name}&r>
        String name_format = FTBRanksAPI.getPermissionValue(player, "ftbranks.name_format").asString().orElse("");
        return channelFormat
                .replace("{prefix}{display_name} :", name_format.replace("{name}", player.getDisplayName().getString()))
                .replace("{suffix}", "") // TODO: add suffix
                .replace("{message}", message);
    }
}
