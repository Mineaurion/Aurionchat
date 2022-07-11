package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<ServerPlayer> {

    private static final LuckPermsUtils luckPermsUtils = AurionChat.luckPermsUtils;

    public AurionChatPlayer(ServerPlayer player, Set<String> channels){
        super(player, AurionChat.config.rabbitmq.serverName.get(), channels);
    }
    @Override
    public boolean hasPermission(String permission) {
        return luckPermsUtils.hasPermission(getUniqueId(), permission);
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(new TextComponent(message.replace("&", "§")), Util.NIL_UUID);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUUID();
    }

    @Override
    public String getPrefix() {
        return luckPermsUtils.getPlayerPrefix(getUniqueId()).orElse("");
    }

    @Override
    public String getSuffix() {
        return luckPermsUtils.getPlayerSuffix(getUniqueId()).orElse("");
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
