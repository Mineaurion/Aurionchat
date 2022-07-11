package com.mineaurion.aurionchat.forge;


import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.mineaurion.aurionchat.forge.command.ChatCommand;
import com.mineaurion.aurionchat.forge.config.Config;
import com.mineaurion.aurionchat.forge.config.ConfigData;
import com.mineaurion.aurionchat.forge.listeners.ChatListener;
import com.mineaurion.aurionchat.forge.listeners.CommandListener;
import com.mineaurion.aurionchat.forge.listeners.LoginListener;
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
    public static Utils utils = new Utils();

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
                (s,b) -> true // I accept anything from the server or the save, if I'm asked
        ));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "aurionchat.toml");
        config = (new ObjectConverter().toObject(Config.SPEC.getValues(), ConfigData::new));

        // Listener
        this.setChatListener(new ChatListener(this));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new LoginListener());
        MinecraftForge.EVENT_BUS.register(this.getChatListener());
    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event){
        new ChatCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public void serverStarted(FMLServerStartedEvent event)
    {
        LOGGER.info("AurionChat Starting");

        try {
            this.setChatService(new ChatService(config.rabbitmq.uri.get(), config.rabbitmq.serverName.get()));
            LOGGER.info("AurionChat Connected to Rabbitmq");
            MinecraftForge.EVENT_BUS.register(new CommandListener(this));
        } catch (IOException|TimeoutException e) {
            MinecraftForge.EVENT_BUS.unregister(this.getChatListener());
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
