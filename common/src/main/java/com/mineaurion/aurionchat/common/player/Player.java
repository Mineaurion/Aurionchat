package com.mineaurion.aurionchat.common.player;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface Player {

    String getDisplayName();

    UUID getUUID();

    void sendMessage(Component message);

    boolean hasPermission(String permission);

    String getPreffix();

    String getSuffix();
}
