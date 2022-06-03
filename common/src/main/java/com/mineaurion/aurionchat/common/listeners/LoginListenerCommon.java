package com.mineaurion.aurionchat.common.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;

import java.util.Map;
import java.util.UUID;

public abstract class LoginListenerCommon<T extends AurionChatPlayerCommon<?>> {
    public Map<UUID, T> aurionChatPlayers;

    public LoginListenerCommon(Map<UUID, T> aurionChatPlayers){
        this.aurionChatPlayers = aurionChatPlayers;
    }

    protected void playerLeaving(UUID uuid){
        this.aurionChatPlayers.remove(uuid);
    };
}