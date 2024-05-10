package com.mineaurion.aurionchat.common.command.sender;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;

import java.util.Objects;
import java.util.UUID;

public abstract class SenderFactory<P extends AurionChatPlugin, T> implements AutoCloseable {

    private final P plugin;

    private LuckPermsUtils luckPermsUtils = null;

    public SenderFactory(P plugin, boolean withLuckPerms){
        this.plugin = plugin;
        if(withLuckPerms){
            luckPermsUtils = new LuckPermsUtils(LuckPermsProvider.get());
        }
    }

    protected P getPlugin(){
        return this.plugin;
    }

    protected abstract UUID getId(T sender);
    protected abstract String getName(T sender);
    protected abstract String getDisplayName(T sender);
    protected abstract void sendMessage(T sender, Component message);
    protected abstract boolean hasPermission(T sender, String permission);
    protected abstract boolean isConsole(T sender);

    public String getPrefix(T player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerPreffix(getId(player)).orElse("") : "";
    };

    public String getSuffix(T player){
        return luckPermsUtils != null ? luckPermsUtils.getPlayerSuffix(getId(player)).orElse("") : "";
    };

    public final ServerPlayer wrap(T sender){
        Objects.requireNonNull(sender, "sender");
        return new AbstractSender<>(this.plugin, this, sender);
    }

    @Override
    public void close() {}
}