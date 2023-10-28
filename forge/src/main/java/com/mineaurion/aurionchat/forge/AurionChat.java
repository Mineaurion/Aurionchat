package com.mineaurion.aurionchat.forge;


import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.logger.PluginLogger;
import com.mineaurion.aurionchat.forge.command.ChatCommand;
import com.mineaurion.aurionchat.forge.listeners.ChatListener;
import com.mineaurion.aurionchat.forge.listeners.LoginListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;

import static net.minecraftforge.fml.ExtensionPoint.DISPLAYTEST;

@Mod(AbstractAurionChat.ID)
public class AurionChat extends AbstractAurionChat {

    private PlayerFactory playerFactory;

    public AurionChat() {
        getlogger().info("AurionChat Initializing");
        ModLoadingContext.get().registerExtensionPoint(DISPLAYTEST, () -> Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY, // ignore me, I'm a server only mod
                (s,b) -> true // I accept anything from the server or the save, if I'm asked
        ));
        MinecraftForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void serverAboutToStart(FMLServerStartingEvent event){
        new ChatCommand(this, event.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event)
    {
        this.enable();
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppedEvent event) {
        this.disable();
    }

    @Override
    protected void registerPlatformListeners() {
        MinecraftForge.EVENT_BUS.register(new LoginListener(this));
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

    public static ITextComponent toNativeText(Component component) {
        return ITextComponent.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }
}
