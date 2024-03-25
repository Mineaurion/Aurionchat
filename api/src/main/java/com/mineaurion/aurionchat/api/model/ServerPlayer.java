package com.mineaurion.aurionchat.api.model;

import net.kyori.adventure.text.Component;

public interface ServerPlayer extends Player {
    void sendMessage(Component message);

    boolean hasPermission(String permission);
}
