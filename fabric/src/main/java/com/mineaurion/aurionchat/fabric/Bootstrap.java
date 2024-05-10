package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.plugin.AurionChatBootstrap;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;
import java.util.Optional;

public class Bootstrap implements AurionChatBootstrap, DedicatedServerModInitializer {
    private final ModContainer loader;

    private AurionChat plugin;


    private MinecraftServer server;

    public Bootstrap(){
        this.loader = FabricLoader.getInstance().getModContainer(AurionChatPlugin.MOD_ID).orElseThrow(() -> new RuntimeException("Could not get the " + AurionChatPlugin.MOD_ID + " mod container"));
    }

    @Override
    public void onInitializeServer(){
        this.plugin = new AurionChat(this);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
        this.plugin.registerListeners();
    }


    @Override
    public Path getConfigDirectory() {
        return FabricLoader.getInstance().getGameDir().resolve("mods").resolve(AurionChatPlugin.MOD_ID);
    }


    public Optional<MinecraftServer> getServer() {
        return Optional.ofNullable(server);
    }

    private void onServerStarting(MinecraftServer server){
        this.server = server;
        this.plugin.enable();
    }

    private void onServerStopping(MinecraftServer server){
        this.plugin.disable();
        this.server = null;
    }
}
