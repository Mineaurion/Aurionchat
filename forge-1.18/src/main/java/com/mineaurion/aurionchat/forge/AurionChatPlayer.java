package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.kyori.adventure.text.Component;
import net.minecraft.Util;
import net.minecraft.server.level.ServerPlayer;

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
    public void sendMessage(Component message) {
        this.player.sendMessage((net.minecraft.network.chat.Component) message, Util.NIL_UUID);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUUID();
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName().getString();
    }

}
