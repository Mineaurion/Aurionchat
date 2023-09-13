package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.server.network.ServerPlayerEntity;

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
    public void sendMessage(Component message) {
        this.player.sendMessage(message);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUuid();
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName().getString();
    }

}
