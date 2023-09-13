package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<ServerPlayer> {

    public AurionChatPlayer(ServerPlayer player, Set<String> channels){
        super(player, channels);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public void sendMessage(Component message) {
        this.player.sendMessage(message);
    }

    @Override
    public UUID getUniqueId() {
        return this.player.uniqueId();
    }

    @Override
    public String getDisplayName() {
        return PlainTextComponentSerializer.plainText().serialize(player.displayName().get());
    }

}
