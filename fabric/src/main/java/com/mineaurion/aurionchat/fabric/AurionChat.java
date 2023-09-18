package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.common.logger.Slf4jPluginLogger;
import com.mineaurion.aurionchat.fabric.command.ChatCommand;
import com.mineaurion.aurionchat.fabric.listeners.ChatListener;
import com.mineaurion.aurionchat.fabric.listeners.LoginListener;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.text.Text;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class AurionChat extends AbstractAurionChat implements DedicatedServerModInitializer {

    private PlayerFactory playerFactory;

    @Override
    public void onInitializeServer() {
        getlogger().info("AurionChat Initializing");
        ServerLifecycleEvents.SERVER_STARTED.register(server -> this.enable());

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> new ChatCommand(this, dispatcher));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> this.disable());
    }

    @Override
    protected void registerPlatformListeners() {
        LoginListener loginListener = new LoginListener(this);
        ChatListener chatListener = new ChatListener(this);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> loginListener.onPlayerJoin(handler.getPlayer()));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> loginListener.onPlayerQuit(handler.getPlayer()));
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(chatListener);
    }

    @Override
    protected void setupPlayerFactory() {
        this.playerFactory = new PlayerFactory();
    }

    @Override
    protected void registerCommands() {
        // Nothing to do here for fabric
    }

    @Override
    protected void disablePlugin() {}

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new FabricConfigAdapter(resolveConfig(AbstractAurionChat.ID + ".conf"));
    }

    @Override
    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    @Override
    protected Path getConfigDirectory() {
        return FabricLoader.getInstance().getGameDir().resolve("mods").resolve(AbstractAurionChat.ID);
    }

    @Override
    public PluginLogger getlogger() {
        return new Slf4jPluginLogger(LoggerFactory.getLogger(AbstractAurionChat.ID));
    }

    public static Text toNativeText(Component component) {
        return Text.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }
}
