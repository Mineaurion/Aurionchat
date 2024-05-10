package com.mineaurion.aurionchat.common.command.sender;

import com.mineaurion.aurionchat.api.model.ServerPlayer;

import java.util.UUID;

public abstract class DummyConsoleSender implements ServerPlayer {

    public DummyConsoleSender(){}

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public boolean isConsole() {
        return true;
    }

    @Override
    public UUID getId() {
        return CONSOLE_UUID;
    }

    @Override
    public String getName() {
        return CONSOLE_NAME;
    }
}
