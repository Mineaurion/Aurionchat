package com.mineaurion.aurionchat.api.model;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface ServerPlayer extends Player {

    UUID CONSOLE_UUID = new UUID(0,0);

    String CONSOLE_NAME = "Console";

    void sendMessage(Component message);

    boolean hasPermission(String permission);

    boolean isConsole();
}
