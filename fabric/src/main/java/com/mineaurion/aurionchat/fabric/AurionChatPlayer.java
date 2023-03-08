package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<ServerPlayerEntity> {

    private static final LuckPermsUtils luckPermsUtils = AurionChat.luckPermsUtils;

    public AurionChatPlayer(ServerPlayerEntity player, Set<String> channels){
        super(player, Config.Rabbitmq.servername, channels);
    }

    @Override
    public boolean hasPermission(String permission) {
        return luckPermsUtils.hasPermission(getUniqueId(), permission);
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(Text.of(message.replace("&", "§")));
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUuid();
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
    public void notifyPlayer() {
        this.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1);
    }
}
