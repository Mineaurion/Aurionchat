package com.mineaurion.aurionchat.common.player;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.kyori.adventure.text.Component;

import java.util.Objects;
import java.util.UUID;

public abstract class PlayerFactory<T> implements AutoCloseable {

    private static final LuckPermsUtils luckPermsUtils = AbstractAurionChat.luckPermsUtils;

    public PlayerFactory(){}

    protected abstract UUID getUUID(T player);
    protected abstract String getName(T player);
    protected abstract void sendMessage(T player, Component message);
    protected abstract boolean hasPermission(T player, String permission);

    public String getPreffix(T player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerPreffix(getUUID(player)).orElse("") : "";
    };

    public String getSuffix(T player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerSuffix(getUUID(player)).orElse("") : "";
    };

    public final Player wrap(T player){
        Objects.requireNonNull(player, "player");
        return new AbstractPlayer<>(this, player);
    }

    @Override
    public void close(){}
}
