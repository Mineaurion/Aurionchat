package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.channel.ChatService;
import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class AurionChat extends JavaPlugin {
    public static Config config;
    public static Map<UUID, AurionChatPlayer> aurionChatPlayers = new HashMap<>();
    public static LuckPermsUtils luckPermsUtils = null;
    public static Utils utils = new Utils();
    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }
    private ChatService chatService;

    public boolean isErrorRabbitmq() {
        return errorRabbitmq;
    }

    public void setErrorRabbitmq(boolean errorRabbitmq) {
        this.errorRabbitmq = errorRabbitmq;
    }

    private boolean errorRabbitmq = false;


    @Override
    public void onEnable(){
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        sendConsoleMessage("&8[&eAurionChat&8]&e - Checking for Vault...");
        RegisteredServiceProvider<LuckPerms> luckPermsprovider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(luckPermsprovider != null) {
            luckPermsUtils = new LuckPermsUtils(luckPermsprovider.getProvider());
        }
        config = new Config(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Config Loaded.");
        sendConsoleMessage("&8[&eAurionChat&8]&e - Registering Listeners");
        setupListener(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Connecting to RabbitMQ");

        try {
            this.setChatService(new ChatService(config.getUri(), config.getServername()));
        } catch (IOException | TimeoutException e){
            Bukkit.getPluginManager().disablePlugin(this);
            this.setErrorRabbitmq(true);
            sendConsoleMessage("&8[&eAurionChat&8]&e - &ccan't connect to rabbitmq, disabling.");
            getLogger().warning(e.getMessage());
        }
        this.getCommand("chat").setExecutor(new ChatCommand());
    }

    private void setupListener(AurionChat plugin){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(), this);
        pluginManager.registerEvents(new ChatListener(plugin), this);
        pluginManager.registerEvents(new CommandListener(plugin), this);
    }

    @Override
    public void onDisable() {
        if(!this.isErrorRabbitmq()){
            try{
                this.getChatService().close();
            }
            catch(IOException|TimeoutException e){
                sendConsoleMessage("&8[&eAurionChat&8]&e - Error when communication closed");
                sendConsoleMessage(e.getMessage());
            }
        }
    }

    public void sendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}