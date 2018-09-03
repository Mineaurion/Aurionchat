package com.mineaurion.aurionchat.bukkit.command;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;

public class ChatCommand implements CommandExecutor {

    private AurionChat plugin;
    public ChatCommand(AurionChat main){
        plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        String command;
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(sender.getName());
        if(cmd.getName().equalsIgnoreCase("chat") || cmd.getName().equalsIgnoreCase("ch")){
            command = (args.length < 1) ? "Default" : args[0];
            switch (command){
                case "join":
                    ChatAction(aurionChatPlayer, args[1],"join");
                    break;
                case "leave":
                    ChatAction(aurionChatPlayer, args[1],"leave");
                    break;
                case "spy":
                    ChatAction(aurionChatPlayer,args[1],"spy");
                    break;
                case "alllisten":
                    ChatAllListen(aurionChatPlayer);
                    break;
                default:
                    StringBuilder message = new StringBuilder();
                    StringBuilder channels = new StringBuilder();
                    for (String channel:aurionChatPlayer.getListening()){
                        channels.append(channel).append(" ");
                    }
                    message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                           .append("&7Spying on channels:&f ").append(channels.toString());
                    aurionChatPlayer.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',message.toString()));
                    break;
            }
            return true;
        }
        return false;
    }

    private void ChatAction(AurionChatPlayer aurionChatPlayer, String channel, String action){
        Set<String> channels = plugin.getConfigPlugin().getAllChannel();
        if(channels.contains(channel)){
            switch(action){
                case "join":
                    aurionChatPlayer.removeListening(aurionChatPlayer.getCurrentChannel());
                    aurionChatPlayer.setCurrentChannel(channel);
                    aurionChatPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have joined the " + channel + " channel.");
                    break;
                case "leave":
                    aurionChatPlayer.removeListening(channel);
                    aurionChatPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have leaved the " + channel + " channel.");
                    break;
                case "spy":
                    aurionChatPlayer.addListening(channel);
                    aurionChatPlayer.getPlayer().sendMessage(ChatColor.GOLD + "You have spy the " + channel + " channel.");
                    break;
            }
        }
        else{
            aurionChatPlayer.getPlayer().sendMessage(ChatColor.RED + "This channel doesn't exist");
        }
    }

    private void ChatAllListen(AurionChatPlayer aurionChatPlayer){
        Set<String> channels = plugin.getConfigPlugin().getAllChannel();
        for(String channel:channels){
            ChatAction(aurionChatPlayer, channel, "spy");
        }
    }


}
