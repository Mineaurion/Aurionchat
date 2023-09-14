package com.mineaurion.aurionchat.common.listeners;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.player.Player;

public abstract class LoginListenerCommon {
    public AbstractAurionChat plugin;

    public LoginListenerCommon(AbstractAurionChat plugin){
        this.plugin = plugin;
    }

    protected void playerLeaving(Player player){
        this.plugin.getAurionChatPlayers().remove(player.getUUID());
    };

    protected void playerJoin(Player player){
        this.plugin.getAurionChatPlayers().putIfAbsent(
            player.getUUID(),
            new AurionChatPlayer(player, plugin)
        );
    }
}