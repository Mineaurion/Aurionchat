package com.mineaurion.aurionchat;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.mineaurion.aurionchat.command.ChatCommand;
import com.mineaurion.aurionchat.config.Config;
import com.mineaurion.aurionchat.config.ConfigData;
import com.mineaurion.aurionchat.listeners.ChatListener;
import com.mineaurion.aurionchat.listeners.CommandListener;
import com.mineaurion.aurionchat.listeners.LoginListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static net.minecraftforge.fml.ExtensionPoint.DISPLAYTEST;

@Mod("aurionchat")
public class AurionChat {
    public static ConfigData config;

    public static Map<UUID, AurionChatPlayer> aurionChatPlayers = new HashMap<>();

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    private ChatService chatService;

    public ChatListener getChatListener() {
        return chatListener;
    }

    public void setChatListener(ChatListener chatListener) {
        this.chatListener = chatListener;
    }

    private ChatListener chatListener;

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    private CommandListener commandListener;

    public boolean isErrorRabbitmq() {
        return errorRabbitmq;
    }

    public void setErrorRabbitmq(boolean error) {
        this.errorRabbitmq = error;
    }

    private boolean errorRabbitmq = false;

    public AurionChat() {
        LOGGER.info("AurionChat Initializing");
        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(DISPLAYTEST, () -> Pair.of(
                () -> FMLNetworkConstants.IGNORESERVERONLY, // ignore me, I'm a server only mod
                (s,b) -> true // i accept anything from the server or the save, if I'm asked
        ));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "aurionchat.toml");
        config = (new ObjectConverter().toObject(Config.SPEC.getValues(), ConfigData::new));

        // Listener
        this.setChatListener(new ChatListener(this));
        this.setCommandListener(new CommandListener(this));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LoginListener());
        MinecraftForge.EVENT_BUS.register(this.getChatListener());
        MinecraftForge.EVENT_BUS.register(this.getCommandListener());
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event){
        ChatCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event)
    {
        LOGGER.info("AurionChat Starting");

        try {
            this.setChatService(new ChatService(config.rabbitmq.uri.get(), config.rabbitmq.serverName.get()));
            LOGGER.info("AurionChat Connected to Rabbitmq");
        } catch (IOException|TimeoutException e) {
            MinecraftForge.EVENT_BUS.unregister(this.getChatListener());
            MinecraftForge.EVENT_BUS.unregister(this.getCommandListener());
            this.setErrorRabbitmq(true);
            LOGGER.error("Aurionchat can't connect to rabbitmq, fallback to standard chat");
            LOGGER.error(e.getMessage());
        }
    }

    @SubscribeEvent
    public void serverStopped(FMLServerStoppedEvent event) throws IOException, TimeoutException {
        if(!this.isErrorRabbitmq()){
            LOGGER.info("AurionChat disconnecting");
            this.getChatService().close();
        }
    }

}
