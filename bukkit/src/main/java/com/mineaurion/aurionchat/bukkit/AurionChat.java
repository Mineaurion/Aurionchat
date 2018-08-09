package com.mineaurion.aurionchat.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import com.mineaurion.aurionchat.bukkit.command.Global;
import com.mineaurion.aurionchat.bukkit.channel.ChatService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class AurionChat extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {

        config = new Config(this);
        config.load();

        String nickname = Bukkit.getMotd();
        boolean stop = false;

        ChatService chatService = getChatService();
        AurionChat plugin = this;

        try{
            Bukkit.getConsoleSender().sendMessage("Nickname is " + nickname);
            chatService.join(nickname, "global");
            Bukkit.getConsoleSender().sendMessage("global channel joined");

        }
        catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }

        getLogger().info("onEnable is called!");
        this.getCommand("g").setExecutor(new Global(plugin));
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
            chatService = new ChatService(config.host);
        }
        catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
        return chatService;
    }


}