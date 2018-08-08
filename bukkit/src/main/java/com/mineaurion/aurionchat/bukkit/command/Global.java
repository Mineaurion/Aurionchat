package com.mineaurion.aurionchat.bukkit.command;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineaurion.aurionchat.bukkit.channel.ChatService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Global implements CommandExecutor {

    private AurionChat plugin;

    public Global(AurionChat main){
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("g")){
            String nickname = Bukkit.getMotd();
            Bukkit.getConsoleSender().sendMessage(nickname);

            ChatService chatService = plugin.getChatService();

            try{
                chatService.send("global",nickname,args[0]);
            }
            catch(IOException e){
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
            }
            Bukkit.getConsoleSender().sendMessage("Send to rabbitmq - Debug");
        }
        return true;
    }
}
