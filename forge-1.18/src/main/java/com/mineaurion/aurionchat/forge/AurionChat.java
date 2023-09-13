package com.mineaurion.aurionchat.forge;


import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.forge.command.ChatCommand;
import com.mineaurion.aurionchat.forge.config.Config;
import com.mineaurion.aurionchat.forge.config.ConfigData;
import com.mineaurion.aurionchat.forge.listeners.ChatListener;
import com.mineaurion.aurionchat.forge.listeners.LoginListener;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.function.Supplier;

@Mod("aurionchat")
public class AurionChat extends AbstractAurionChat<AurionChatPlayer> {

    public static final String ID = "aurionchat";
    public static ConfigData config;
    public static Path channelsJsonPath;


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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "aurionchat.toml");
        config = (new ObjectConverter().toObject(Config.SPEC.getValues(), ConfigData::new));

        MinecraftForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void serverAboutToStart(ServerAboutToStartEvent event){
        channelsJsonPath = ServerLifecycleHooks.getCurrentServer().getWorldPath(new LevelResource("serverconfig/aurionchat-channels.json"));
        new ChatCommand(this, event.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event)
    {
        this.enable(
                config.rabbitmq.uri.get(),
                config.options.spy.get(),
                config.options.autoMessage.get(),
                true
        );
    }

    @SubscribeEvent
    public void serverStopped(ServerStoppedEvent event) {
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        MinecraftForge.EVENT_BUS.register(new LoginListener(this));
        MinecraftForge.EVENT_BUS.register(new ChatListener(this));
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
    public PluginLogger getlogger() {
        return new Log4jPluginLogger(LogManager.getLogger(AurionChat.ID));
    }
}
