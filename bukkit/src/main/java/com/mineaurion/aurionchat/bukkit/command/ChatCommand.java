package com.mineaurion.aurionchat.bukkit.command;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ChatCommand implements CommandExecutor {
    private enum Action {
        JOIN,
        LEAVE,
        SPY,
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        String command;
        Player player = Bukkit.getPlayer(sender.getName());
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());
        if(cmd.getName().equalsIgnoreCase("chat") || cmd.getName().equalsIgnoreCase("ch")){
            command = (args.length < 1) ? "Default" : args[0];
            switch (command){
                case "join":
                case "j":
                    ChatAction(player, aurionChatPlayer, args[1], Action.JOIN);
                    break;
                case "leave":
                case "l":
                    ChatAction(player, aurionChatPlayer, args[1], Action.LEAVE);
                    break;
                case "spy":
                case "s":
                    ChatAction(player, aurionChatPlayer,args[1], Action.SPY);
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
                    for(String avalaibleChannel: AurionChat.config.getAllChannel()) {
                        avalaibleChannels.append(avalaibleChannel).append(" ");
                    }
                    message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                           .append("&7Spying on channels:&f ").append(channels).append("\n")
                           .append("&7Avalaible channels:&f ").append(avalaibleChannels);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',message.toString()));
                    break;
            }
            return true;
        }
        return false;
    }

    private void ChatAction(Player player, AurionChatPlayer aurionChatPlayer, String channel, Action action){
        Set<String> channels = AurionChat.config.getAllChannel();
        if(channels.contains(channel)){
            switch(action){
                case JOIN:
                    aurionChatPlayer.removeChannel(aurionChatPlayer.getCurrentChannel());
                    aurionChatPlayer.setCurrentChannel(channel);
                    player.sendMessage(ChatColor.GOLD + "You have joined the " + channel + " channel.");
                    break;
                case LEAVE:
                    aurionChatPlayer.removeChannel(channel);
                    player.sendMessage(ChatColor.GOLD + "You have leaved the " + channel + " channel.");
                    break;
                case SPY:
                    aurionChatPlayer.addChannel(channel);
                    player.sendMessage(ChatColor.GOLD + "You have spy the " + channel + " channel.");
                    break;
            }
        }
        else{
           player.sendMessage(ChatColor.RED + "This channel doesn't exist");
        }
    }

    private void ChatAllListen(Player player, AurionChatPlayer aurionChatPlayer){
        for(String channel: AurionChat.config.getAllChannel()){
            ChatAction(player, aurionChatPlayer, channel, Action.SPY);
        }
    }


}
