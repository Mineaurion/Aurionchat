package com.mineaurion.aurionchat.forge;


import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import com.mineaurion.aurionchat.forge.command.ChatCommand;
import com.mineaurion.aurionchat.forge.config.Config;
import com.mineaurion.aurionchat.forge.config.ConfigData;
import com.mineaurion.aurionchat.forge.listeners.ChatListener;
import com.mineaurion.aurionchat.forge.listeners.CommandListener;
import com.mineaurion.aurionchat.forge.listeners.LoginListener;
import net.luckperms.api.LuckPermsProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

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

    public static LuckPermsUtils luckPermsUtils = null;

    public boolean isErrorRabbitmq() {
        return errorRabbitmq;
    }

    public void setErrorRabbitmq(boolean error) {
        this.errorRabbitmq = error;
    }

    private boolean errorRabbitmq = false;

    public AurionChat() {
        LOGGER.info("AurionChat Initializing");
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


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void onRegisterCommandEvent(RegisterCommandsEvent event){
        new ChatCommand(event.getDispatcher(), event.getEnvironment());
    }

    @SubscribeEvent
    public void serverStarted(ServerStartedEvent event)
    {
        // Listener
        ChatListener chatListener = new ChatListener(this);
        luckPermsUtils = new LuckPermsUtils(LuckPermsProvider.get());
        LOGGER.info("AurionChat Starting");
        try {
            this.setChatService(new ChatService(config.rabbitmq.uri.get(), config.rabbitmq.serverName.get()));
            LOGGER.info("AurionChat Connected to Rabbitmq");
            MinecraftForge.EVENT_BUS.register(new LoginListener());
            MinecraftForge.EVENT_BUS.register(chatListener);
            MinecraftForge.EVENT_BUS.register(new CommandListener(this));
        } catch (IOException|TimeoutException e) {
            MinecraftForge.EVENT_BUS.unregister(chatListener);
            this.setErrorRabbitmq(true);
            LOGGER.error("Aurionchat can't connect to rabbitmq, fallback to standard chat");
            LOGGER.error(e.getMessage());
        }
    }

    @SubscribeEvent
    public void serverStopped(ServerStoppedEvent event) throws IOException, TimeoutException {
        if(!this.isErrorRabbitmq()){
            LOGGER.info("AurionChat disconnecting");
            this.getChatService().close();
        }
    }

}
