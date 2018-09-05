package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.bukkit.channel.ChatServiceBukkit;
import com.mineaurion.aurionchat.bukkit.command.ChatCommand;
import com.mineaurion.aurionchat.bukkit.listeners.CommandListener;
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
import java.util.*;
import java.util.concurrent.TimeoutException;

public class AurionChat extends JavaPlugin {

    private Config config;
    private LoginListener loginListener;
    private ChatListener chatListener;
    private CommandListener commandListener;

    private static AurionChat instance;
    public static final String CHANNEL = "aurionchat";

    public static Set<AurionChatPlayer> players = new HashSet<>();
    public static Set<AurionChatPlayer> onlinePlayers = new HashSet<>();


    public static Chat chat = null;
    public static Permission permission = null;

    public CommandMap commandmap;


    public AurionChat(){
        instance = this;
    }


    @Override
    public void onEnable(){
        AurionChat plugin = this;

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - Initializing..."));
        config = getConfigPlugin();
        config.load();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - Config Loaded."));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - Checking for Vault..."));
        if(!setupPermissions()){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - &cCould not find Vault dependency, disabling."));
            Bukkit.getPluginManager().disablePlugin(this);
        }
        setupChat();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - Enabled Successfully"));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - Registering Listeners"));
        setupListener(plugin);
        setupChannels(config);

        this.getCommand("chat").setExecutor(new ChatCommand(plugin));
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
            getChatService().join(config.servername);
        }
        catch(IOException e){
            getLogger().warning(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
        ChatServiceBukkit chatService = getChatService();
        try{
            chatService.close();
        }
        catch(Exception e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eAurionChat&8]&e - Error when communication closed"));
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    public ChatServiceBukkit getChatService(){
        ChatServiceBukkit chatService = null;
        try {
            chatService = new ChatServiceBukkit(config.host, this);
        }
        catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
        return chatService;
    }

    public Config getConfigPlugin(){
        return new Config(this);
    }

    public Utils getUtils(){
        return new Utils(this);
    }
}