package com.mineaurion.aurionchat.forge;


import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.plugin.AbstractAurionChat;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import com.mineaurion.aurionchat.forge.listeners.ChatListener;
import com.mineaurion.aurionchat.forge.listeners.LoginListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;

public class AurionChat extends AbstractAurionChat {

    private Bootstrap bootstrap;

    private SenderFactory senderFactory;

    private CommandExecutor commandManager;

    public AurionChat(Bootstrap bootstrap){
        super(new Log4jPluginLogger(LogManager.getLogger(AurionChatPlugin.NAME)));
        this.bootstrap = bootstrap;
    }

    @Override
    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    protected void registerEarlyListeners(){
        this.commandManager = new CommandExecutor(this);
        this.bootstrap.registerListeners(this.commandManager);
    }

    @Override
    protected void setupSenderFactory() {
        this.senderFactory = new SenderFactory(this);
    }

    public SenderFactory getSenderFactory() {
        return senderFactory;
    }

    @Override
    protected void registerPlatformListeners() {
        MinecraftForge.EVENT_BUS.register(new LoginListener(this));
        MinecraftForge.EVENT_BUS.register(new ChatListener(this));
    }

    @Override
    protected void registerCommands() {
        // Not used for forge, registered in #registerEarlyListeners
    }

    @Override
    protected void disablePlugin() {
        // TODO: need to be tweaked to remove all listener
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new ForgeConfigAdapter(resolveConfig(AurionChatPlugin.MOD_ID + ".conf"));
    }

    public static net.minecraft.network.chat.Component toNativeText(Component component){
        return net.minecraft.network.chat.Component.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }
}
