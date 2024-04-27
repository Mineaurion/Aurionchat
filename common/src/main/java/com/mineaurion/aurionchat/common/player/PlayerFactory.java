package com.mineaurion.aurionchat.common.player;

import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;

import java.util.Objects;
import java.util.UUID;

public abstract class PlayerFactory<T> implements AutoCloseable {

    private LuckPermsUtils luckPermsUtils = null;

    public PlayerFactory(boolean withLuckPerms){
        if(withLuckPerms){
            luckPermsUtils = new LuckPermsUtils(LuckPermsProvider.get());
        }
    }

    protected abstract UUID getUUID(T player);
    protected abstract String getName(T player);
    protected abstract void sendMessage(T player, Component message);

    public String getPreffix(T player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerPreffix(getUUID(player)).orElse("") : "";
    };

    public String getSuffix(T player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerSuffix(getUUID(player)).orElse("") : "";
    };

    protected boolean hasPermission(T player, String permission, boolean explicitly) {
        return luckPermsUtils.hasPermission(getUUID(player), permission, explicitly);
    }

    public final Player wrap(T player){
        Objects.requireNonNull(player, "player");
        return new AbstractPlayer<>(this, player);
    }

    @Override
    public void close(){}
}
