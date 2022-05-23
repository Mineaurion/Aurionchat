package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.channel.ChatService;
import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.LuckPermsUtils;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
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

    public static Map<UUID, AurionChatPlayer> aurionChatPlayers = new HashMap<>();

    public static Config config;
    public static Chat chat = null;
    public static Permission permission = null;
    public static LuckPermsUtils luckPermsUtils = null;
    public ChatService getChatService() {
        return chatService;
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }
    private ChatService chatService;


    @Override
    public void onEnable(){
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        sendConsoleMessage("&8[&eAurionChat&8]&e - Checking for Vault...");
        if(!setupPermissions()){
            sendConsoleMessage("&8[&eAurionChat&8]&e - &cCould not find Vault dependency, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if(chatProvider != null){
            chat = chatProvider.getProvider();
        }
        RegisteredServiceProvider<LuckPerms> luckPermsprovider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if(luckPermsprovider != null) {
            luckPermsUtils = new LuckPermsUtils(luckPermsprovider.getProvider());
        }
        config = new Config(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Config Loaded.");

        sendConsoleMessage("&8[&eAurionChat&8]&e - Enabled Successfully");
        sendConsoleMessage("&8[&eAurionChat&8]&e - Registering Listeners");
        setupListener(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Connecting to RabbitMQ");

        try {
            this.setChatService(new ChatService(config.getUri(), config.getServername()));
        } catch (IOException | TimeoutException e){
            Bukkit.getPluginManager().disablePlugin(this);
            sendConsoleMessage("&8[&eAurionChat&8]&e - &ccan't connect to rabbitmq, disabling.");
            getLogger().warning(e.getMessage());
        }
        getCommand("chat").setExecutor(new ChatCommand());
    }

    private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if(permissionProvider != null){
            permission = permissionProvider.getProvider();
        }
        return permission != null;
    }

    private void setupListener(AurionChat plugin){
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new LoginListener(), this);
        pluginManager.registerEvents(new ChatListener(plugin), this);
        pluginManager.registerEvents(new CommandListener(plugin), this);
    }

    @Override
    public void onDisable() {
        try{
            this.getChatService().close();
        }
        catch(IOException|TimeoutException e){
            sendConsoleMessage("&8[&eAurionChat&8]&e - Error when communication closed");
            sendConsoleMessage(e.getMessage());
        }
    }

    public void sendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}