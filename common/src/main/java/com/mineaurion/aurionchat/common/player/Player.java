package com.mineaurion.aurionchat.common.player;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface Player {

    String getDisplayName();

    UUID getUUID();

    void sendMessage(Component message);

    default boolean hasPermission(String permission) {
        return hasPermission(permission, false);
    }

    boolean hasPermission(String permission, boolean explicitly);

    String getPreffix();

    String getSuffix();
}
