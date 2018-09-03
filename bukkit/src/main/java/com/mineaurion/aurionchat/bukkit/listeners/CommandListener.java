package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.Set;

public class CommandListener implements CommandExecutor, Listener {
    private AurionChat plugin;

    public CommandListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        Set<String> chatChannels = plugin.getConfigPlugin().getAllChannel();
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(event.getPlayer());

        String command = event.getMessage().substring(1).split(" ")[0];
        String message = (event.getMessage().length() == command.length() + 1) ? event.getMessage().substring(command.length() + 1) : event.getMessage().substring(command.length() + 2);

        for(String channel: chatChannels){
            String channelAlias = plugin.getConfigPlugin().getChannelAlias(channel);

            if(command.equalsIgnoreCase(channel) || command.equalsIgnoreCase(channelAlias)){
                if(event.getMessage().length() == command.length() + 1){
                    aurionChatPlayer.getPlayer().sendMessage(ChatColor.RED + "Invalid command : /" + channel + " <message>");
                    event.setCancelled(true);
                    return;
                }
                aurionChatPlayer.addListening(channel);
                String sendFormat = plugin.getUtils().processMessage(channel,message,aurionChatPlayer.getPlayer());

                try{
                    plugin.getChatService().send(channel,sendFormat);
                }
                catch(IOException e){
                    Bukkit.getConsoleSender().sendMessage(e.getMessage());
                }
                event.setCancelled(true);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            this.plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "This command must be run by a player.");
            return true;
        }
        return true;
    }

}
