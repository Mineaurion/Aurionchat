package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<Player> {
    public AurionChatPlayer(Player player, Set<String> channels){
        super(player, channels);
    }
    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public void sendMessage(Component message) {
        AurionChat.audiences.player(player).sendMessage(message);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getDisplayName() {
        return this.player.getDisplayName();
    }

}
