package com.mineaurion.aurionchat.bukkit;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerFactory extends com.mineaurion.aurionchat.common.player.PlayerFactory<Player> {

    private final BukkitAudiences audiences;

    public PlayerFactory(AurionChat plugin) {
        super(true);
        this.audiences = plugin.getAudiences();
    }

    @Override
    protected UUID getUUID(Player player) {
        return player.getUniqueId();
    }

    @Override
    protected String getName(Player player) {
        return player.getName();
    }

    @Override
    protected void sendMessage(Player player, Component message) {
        this.audiences.player(player).sendMessage(message);
    }

    @Override
    protected boolean hasPermission(Player player, String permission, boolean explicitly) {
        return explicitly
                ? player.getEffectivePermissions().stream()
                .anyMatch(perm -> permission.equals(perm.getPermission()))
                : player.hasPermission(permission);
    }
}
