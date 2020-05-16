package com.mineaurion.aurionchat.bukkit.command;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ChatCommand implements CommandExecutor {

    private AurionChat plugin;
    private AurionChatPlayers aurionChatPlayers;
    public ChatCommand(AurionChat plugin){
        this.plugin = plugin;
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        String command;
        Player player = Bukkit.getPlayer(sender.getName());
        AurionChatPlayer aurionChatPlayer = aurionChatPlayers.getAurionChatPlayer(player.getUniqueId());
        if(cmd.getName().equalsIgnoreCase("chat") || cmd.getName().equalsIgnoreCase("ch")){
            command = (args.length < 1) ? "Default" : args[0];
            switch (command){
                case "join":
                case "j":
                    ChatAction(player, aurionChatPlayer, args[1],"join");
                    break;
                case "leave":
                case "l":
                    ChatAction(player, aurionChatPlayer, args[1],"leave");
                    break;
                case "spy":
                case "s":
                    ChatAction(player, aurionChatPlayer,args[1],"spy");
                    break;
                case "alllisten":
                    ChatAllListen(player, aurionChatPlayer);
                    break;
                default:
                    StringBuilder message = new StringBuilder();
                    StringBuilder channels = new StringBuilder();
                    StringBuilder avalaibleChannels = new StringBuilder();
                    for (String channel: aurionChatPlayer.getChannels()){
                        channels.append(channel).append(" ");
                    }
                    for(String avalaibleChannel: plugin.getConfigPlugin().getAllChannel()) {
                        avalaibleChannels.append(avalaibleChannel).append(" ");
                    }
                    message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                           .append("&7Spying on channels:&f ").append(channels.toString()).append("\n")
                           .append("&7Avalaible channels:&f ").append(avalaibleChannels.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',message.toString()));
                    break;
            }
            return true;
        }
        return false;
    }

    private void ChatAction(Player player, AurionChatPlayer aurionChatPlayer, String channel, String action){
        Set<String> channels = plugin.getConfigPlugin().getAllChannel();
        if(channels.contains(channel)){
            switch(action){
                case "join":
                    aurionChatPlayer.removeListening(aurionChatPlayer.getCurrentChannel());
                    aurionChatPlayer.setCurrentChannel(channel);
                    player.sendMessage(ChatColor.GOLD + "You have joined the " + channel + " channel.");
                    break;
                case "leave":
                    aurionChatPlayer.removeListening(channel);
                    player.sendMessage(ChatColor.GOLD + "You have leaved the " + channel + " channel.");
                    break;
                case "spy":
                    aurionChatPlayer.addListening(channel);
                    player.sendMessage(ChatColor.GOLD + "You have spy the " + channel + " channel.");
                    break;
            }
        }
        else{
           player.sendMessage(ChatColor.RED + "This channel doesn't exist");
        }
    }

    private void ChatAllListen(Player player, AurionChatPlayer aurionChatPlayer){
        for(String channel: plugin.getConfigPlugin().getAllChannel()){
            ChatAction(player, aurionChatPlayer, channel, "spy");
        }
    }


}
