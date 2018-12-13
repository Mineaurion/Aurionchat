package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.channel.ChatService;
import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import net.milkbowl.vault.chat.Chat;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;

import java.io.IOException;

public class AurionChat extends JavaPlugin {

    private Config config;
    private AurionChatPlayers aurionChatPlayers;
    private Utils utils;
    private LoginListener loginListener;
    private ChatListener chatListener;
    private CommandListener commandListener;

    public static final String CHANNEL = "aurionchat";

    public static Chat chat = null;
    public static Permission permission = null;

    public CommandMap commandmap;


    public AurionChat(){

    }


    @Override
    public void onEnable(){
        sendConsoleMessage("&8[&eAurionChat&8]&e - Initializing...");
        config = new Config(this);
        aurionChatPlayers = new AurionChatPlayers();
        utils = new Utils(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Config Loaded.");
        sendConsoleMessage("&8[&eAurionChat&8]&e - Checking for Vault...");
        if(!setupPermissions()){
            sendConsoleMessage("&8[&eAurionChat&8]&e - &cCould not find Vault dependency, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        setupChat();
        sendConsoleMessage("&8[&eAurionChat&8]&e - Enabled Successfully");
        sendConsoleMessage("&8[&eAurionChat&8]&e - Registering Listeners");
        setupListener(this);
        sendConsoleMessage("&8[&eAurionChat&8]&e - Connecting to RabbitMQ");
        setupChannels(config);

        getCommand("chat").setExecutor(new ChatCommand(this));
    }

    private void setupChat(){
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
        if(chatProvider != null){
            chat = (Chat)chatProvider.getProvider();
        }
        //return chat != null;
    }

    private boolean setupPermissions(){
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if(permissionProvider != null){
            permission = (Permission)permissionProvider.getProvider();
        }
        return permission != null;
    }

    private void setupListener(AurionChat plugin){
        PluginManager pluginManager = getServer().getPluginManager();
        this.loginListener = new LoginListener(plugin);
        this.chatListener  = new ChatListener(plugin);
        this.commandListener = new CommandListener(plugin);
        pluginManager.registerEvents(this.loginListener, this);
        pluginManager.registerEvents(this.chatListener, this);
        pluginManager.registerEvents(this.commandListener, this);
    }

    private void setupChannels(Config config){
        try{
            getChatService().join(config.getServername());
        }
        catch(IOException e){
            getLogger().warning(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        ChatService chatService = getChatService();
        try{
            chatService.close();
        }
        catch(Exception e){
            sendConsoleMessage("&8[&eAurionChat&8]&e - Error when communication closed");
            sendConsoleMessage(e.getMessage());
        }
    }

    public ChatService getChatService(){
        return new ChatService(config.getUri(), this);
    }

    public Config getConfigPlugin(){
        return this.config;
    }

    public Utils getUtils(){
        return this.utils;
    }

    public AurionChatPlayers getAurionChatPlayers(){
        return this.aurionChatPlayers;
    }

    public void sendConsoleMessage(String message){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}