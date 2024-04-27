package com.mineaurion.aurionchat.forge;


import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.forge.command.ChatCommand;
import com.mineaurion.aurionchat.forge.listeners.ChatListener;
import com.mineaurion.aurionchat.forge.listeners.PlayerListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.function.Supplier;

@Mod(AbstractAurionChat.ID)
public class AurionChat extends AbstractAurionChat {

    private PlayerFactory playerFactory;

    public AurionChat() {
        getlogger().info("AurionChat Initializing");
        try {
            ModLoadingContext.class.getDeclaredMethod("registerExtensionPoint", Class.class, Supplier.class)
                    .invoke(
                            ModLoadingContext.get(),
                            DisplayTest.class,
                            (Supplier<?>) () -> new DisplayTest(
                                    () -> NetworkConstants.IGNORESERVERONLY,
                                    (a, b) -> true
                            )
                    );
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MinecraftForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void serverAboutToStart(ServerAboutToStartEvent event){
        new ChatCommand(this, event.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event)
    {
        this.enable();
    }

    @SubscribeEvent
    public void serverStopped(ServerStoppedEvent event) {
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener(this));
        MinecraftForge.EVENT_BUS.register(new ChatListener(this));
    }

    @Override
    protected void setupPlayerFactory() {
        this.playerFactory = new PlayerFactory();
    }

    @Override
    protected void registerCommands() {
        // Nothing to do here for forge
    }

    @Override
    protected void disablePlugin() {
        // TODO: need to be tweaked to remove all listener
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new ForgeConfigAdapter(resolveConfig(AbstractAurionChat.ID + ".conf"));
    }

    @Override
    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    @Override
    protected Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get().resolve(AbstractAurionChat.ID).toAbsolutePath();
    }

    @Override
    public PluginLogger getlogger() {
        return new Log4jPluginLogger(LogManager.getLogger(AurionChat.ID));
    }

    public static net.minecraft.network.chat.Component toNativeText(Component component){
        return net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }
}
