package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;

import java.util.UUID;

public class SenderFactory extends com.mineaurion.aurionchat.common.command.sender.SenderFactory<AurionChat, Audience> {

    public SenderFactory(AurionChat plugin){
        super(plugin, true);
    }

    @Override
    protected String getName(Audience source) {
        if(source instanceof Player){
            return ((Player) source).name();
        }
        return ServerPlayer.CONSOLE_NAME;
    }

    // TODO: review this, it's copy/paste from getName
    @Override
    protected String getDisplayName(Audience sender) {
        if(sender instanceof Player){
            return ((Player) sender).name();
        }
        return ServerPlayer.CONSOLE_NAME;
    }

    @Override
    protected UUID getId(Audience source) {
        if(source instanceof Player){
            return ((Player) source).uniqueId();
        }
        return ServerPlayer.CONSOLE_UUID;
    }

    @Override
    protected void sendMessage(Audience source, Component message) {
        source.sendMessage(message);
    }

    @Override
    protected boolean hasPermission(Audience source, String permission) {
        if(!(source instanceof Subject)){
            throw new IllegalStateException("Source is not a subject");
        }
        return ((Subject) source).hasPermission(permission);
    }

    @Override
    protected boolean isConsole(Audience sender) {
        return sender instanceof SystemSubject;
    }
}