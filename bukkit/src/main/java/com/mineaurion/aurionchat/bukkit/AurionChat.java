package com.mineaurion.aurionchat.bukkit;

import net.milkbowl.vault.chat.Chat;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import com.mineaurion.aurionchat.bukkit.command.Global;
import com.mineaurion.aurionchat.bukkit.channel.ChatService;
import com.mineaurion.aurionchat.bukkit.listeners.LoginListener;
import com.mineaurion.aurionchat.bukkit.listeners.ChatListener;

import com.mineaurion.aurionchat.bukkit.utilities.Format;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;


public class AurionChat extends JavaPlugin {

    private Config config;
    private LoginListener loginListener;
    private ChatListener chatListener;

    public static final String CHANNEL = "aurionchat";

    public static Set<AurionChatPlayer> players = new HashSet<>();
    public static Set<AurionChatPlayer> onlinePlayers = new HashSet<>();


    public static Chat chat = null;
    public static Permission permission = null;

    public AurionChat(){
    }


    @Override
    public void onEnable(){
        AurionChat plugin = this;

        Bukkit.getConsoleSender().sendMessage(Format.FormatStringAll("&8[&eAurionChat&8]&e - Initializing..."));
        config = getConfigPlugin();
        config.load();
        Bukkit.getConsoleSender().sendMessage(Format.FormatStringAll("&8[&eAurionChat&8]&e - Config Loaded."));
        Bukkit.getConsoleSender().sendMessage(Format.FormatStringAll("&8[&eAurionChat&8]&e - Checking for Vault..."));
        if(!setupPermissions()){
            Bukkit.getConsoleSender().sendMessage(Format.FormatStringAll("&8[&eAurionChat&8]&e - &cCould not find Vault dependency, disabling."));
            Bukkit.getPluginManager().disablePlugin(this);
        }
        setupChat();
        Bukkit.getConsoleSender().sendMessage(Format.FormatStringAll("&8[&eAurionChat&8]&e - Enabled Successfully"));
        Bukkit.getConsoleSender().sendMessage(Format.FormatStringAll("&8[&eAurionChat&8]&e - Registering Listeners"));
        setupListener(plugin);
        setupChannels();
        this.getCommand("g").setExecutor(new Global(plugin));
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
        pluginManager.registerEvents(this.loginListener, this);
        pluginManager.registerEvents(this.chatListener, this);
    }

    private void setupChannels(){
        try{
            getChatService().join(Bukkit.getMotd());
        }
        catch(IOException e){
            getLogger().warning(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
//        ChatService chatService = getChatService();
//
//        List<String> messages = chatService.takeMessages();
//        for(String m:messages){
//            Bukkit.getConsoleSender().sendMessage(m);
//
//        }
    }


    public ChatService getChatService(){
        ChatService chatService = null;
        try {
            chatService = new ChatService(config.host, this);
        }
        catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
        return chatService;
    }

    public Config getConfigPlugin(){
        return new Config(this);
    }

    public void sendMessageToPlayer(String channelName, String message){
        for(AurionChatPlayer p: AurionChat.onlinePlayers){
            Set<String> listening = p.getListening();
            if(listening.contains(channelName)){
                p.getPlayer().sendMessage(message);
            }
        }

    }

}