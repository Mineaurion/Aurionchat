package com.mineaurion.aurionchat.common.player;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public class AbstractPlayer<T> implements Player {

    private final PlayerFactory<T> factory;
    private final T player;

    private final UUID uuid;
    private final String name;

    public AbstractPlayer(PlayerFactory<T> factory, T player){
        this.factory = factory;
        this.player = player;
        this.uuid = factory.getUUID(player);
        this.name = factory.getName(player);
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public void sendMessage(Component message) {
        this.factory.sendMessage(player, message);
    }

    @Override
    public boolean hasPermission(String permission, boolean explicitly) {
        return this.factory.hasPermission(player, permission, explicitly);
    }

    @Override
    public String getPreffix() {
        return this.factory.getPreffix(player);
    }

    @Override
    public String getSuffix() {
        return this.factory.getSuffix(player);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
