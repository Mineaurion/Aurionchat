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
import net.minecraft.world.storage.FolderName;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;

import static net.minecraftforge.fml.ExtensionPoint.DISPLAYTEST;

@Mod("aurionchat")
public class AurionChat extends AbstractAurionChat<AurionChatPlayer> {

    public static final String ID = "aurionchat";
    public static ConfigData config;
    public static Path channelsJsonPath;


    public AurionChat() {
        getlogger().info("AurionChat Initializing");
        ModLoadingContext.get().registerExtensionPoint(DISPLAYTEST, () -> Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY, // ignore me, I'm a server only mod
                (s,b) -> true // I accept anything from the server or the save, if I'm asked
        ));
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC, "aurionchat.toml");
        config = (new ObjectConverter().toObject(Config.SPEC.getValues(), ConfigData::new));

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void serverAboutToStart(FMLServerAboutToStartEvent event){
        channelsJsonPath = ServerLifecycleHooks.getCurrentServer().getWorldPath(new FolderName("serverconfig/aurionchat-channels.json"));
        new ChatCommand(this, event.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event)
    {
        this.enable(
                config.rabbitmq.uri.get(),
                config.options.spy.get(),
                config.options.autoMessage.get(),
                false
        );
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppedEvent event) {
        this.disable();
    }

    @Override
    protected void registerPlatformListeners(){
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
