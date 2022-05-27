package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon<Player> {

    private static final LuckPermsUtils luckPermsUtils = AurionChat.luckPermsUtils;
    public AurionChatPlayer(Player player, Set<String> channels){
        super(player, channels);
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        this.player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public String getPrefix() {
        return luckPermsUtils.getPlayerPrefix(this.getUniqueId());
    }

    @Override
    public String getSuffix() {
        return luckPermsUtils.getPlayerSuffix(this.getUniqueId());
    }

    @Override
    public String getDisplayName() {
        return TextSerializers.FORMATTING_CODE.serialize(player.getDisplayNameData().displayName().get());
    }

    @Override
    public void notifyPlayer(){
        this.player.playSound( AurionChat.config.options.sound, this.player.getLocation().getPosition(), 10.0);
        this.player.sendTitle(Title.builder().subtitle(TextSerializers.FORMATTING_CODE.deserialize("Mention")).fadeIn(20).fadeOut(20).stay(40).build());
    }
}
