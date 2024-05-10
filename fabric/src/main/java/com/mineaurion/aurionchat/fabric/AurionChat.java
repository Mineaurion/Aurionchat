package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.Log4jPluginLogger;
import com.mineaurion.aurionchat.common.plugin.AbstractAurionChat;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import com.mineaurion.aurionchat.fabric.listeners.ChatListener;
import com.mineaurion.aurionchat.fabric.listeners.LoginListener;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;

public class AurionChat extends AbstractAurionChat {

    public final Bootstrap bootstrap;

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

    @Override
    protected void setupSenderFactory() {
        this.senderFactory = new SenderFactory(this);
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
    protected void registerCommands() {
        // Nothing to do here for fabric
    }

    @Override
    protected void disablePlugin() {}

    protected void registerListeners(){
        this.commandManager = new CommandExecutor(this);
        this.commandManager.register();
    }

    public static Text toNativeText(Component component) {
        return Text.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return new FabricConfigAdapter(resolveConfig(AbstractAurionChat.MOD_ID + ".conf"));
    }

    public SenderFactory getSenderFactory() {
        return senderFactory;
    }
}
