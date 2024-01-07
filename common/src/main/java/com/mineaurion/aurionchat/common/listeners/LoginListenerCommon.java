package com.mineaurion.aurionchat.common.listeners;

import com.mineaurion.aurionchat.api.AurionPacket;
import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.api.model.Player;
import com.mineaurion.aurionchat.common.player.PlayerEvent;
import net.kyori.adventure.text.Component;

import static com.mineaurion.aurionchat.api.AurionPacket.Type.*;
import static com.mineaurion.aurionchat.api.AurionPacket.Type.DEATH;

public abstract class LoginListenerCommon<T extends AbstractAurionChat> {
    public T plugin;

    public LoginListenerCommon(T plugin){
        this.plugin = plugin;
    }

    protected void playerJoin(Player player){
        AurionChatPlayer plr = new AurionChatPlayer(player, plugin);
        this.plugin.getAurionChatPlayers().putIfAbsent(player.getId(), plr);
        //todo: needs join/leave message pattern
        String txt = plr.getBestName() + " joined the game";
        Component component = Component.text(txt);
        AurionPacket.Builder packet = AurionPacket.event(plr, JOIN, component)
                .detail(txt);
        try {
            this.plugin.getChatService().send(packet);
        } catch (Throwable e) {
            plugin.getlogger().severe("Failed to send PlayerJoin event", e);
        }
    }

    protected void playerLeaving(Player player){
        AurionChatPlayer plr = this.plugin.getAurionChatPlayers().remove(player.getId());
        //todo: needs join/leave message pattern
        String txt = plr.getBestName() + " left the game";
        Component component = Component.text(txt);
        AurionPacket.Builder packet = AurionPacket.event(plr, LEAVE, component)
                .detail(txt);
        try {
            this.plugin.getChatService().send(packet);
        } catch (Throwable e) {
            plugin.getlogger().severe("Failed to send PlayerLeave event", e);
        }
    }

    protected void playerAdvancement(Player player, String sanitizedAdvancementMessage){
        AurionChatPlayer plr = this.plugin.getAurionChatPlayers().remove(player.getId());
        Component component = Component.text(sanitizedAdvancementMessage);
        AurionPacket.Builder packet = AurionPacket.event(plr, ADVANCEMENT, component)
                .detail(sanitizedAdvancementMessage);
        try {
            this.plugin.getChatService().send(packet);
        } catch (Throwable e) {
            plugin.getlogger().severe("Failed to send PlayerAdvancement event", e);
        }
    }

    protected void playerDeath(Player player, String sanitizedDeathMessage){
        AurionChatPlayer plr = this.plugin.getAurionChatPlayers().remove(player.getId());
        Component component = Component.text(sanitizedDeathMessage);
        AurionPacket.Builder packet = AurionPacket.event(plr, DEATH, component)
                .detail(sanitizedDeathMessage);
        try {
            this.plugin.getChatService().send(packet);
        } catch (Throwable e) {
            plugin.getlogger().severe("Failed to send PlayerDeath event", e);
        }
    }

    public T getPlugin(){
        return this.plugin;
    }
}