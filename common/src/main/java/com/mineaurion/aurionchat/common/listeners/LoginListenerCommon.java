package com.mineaurion.aurionchat.common.listeners;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.api.model.Player;

public abstract class LoginListenerCommon<T extends AbstractAurionChat> {
    public T plugin;

    public LoginListenerCommon(T plugin){
        this.plugin = plugin;
    }

    protected void playerLeaving(ServerPlayer player){
        this.plugin.getAurionChatPlayers().remove(player.getId());
    };

    protected void playerJoin(ServerPlayer player){
        this.plugin.getAurionChatPlayers().putIfAbsent(
            player.getId(),
            new AurionChatPlayer(player, plugin)
        );
    }

    public T getPlugin(){
        return this.plugin;
    }
}