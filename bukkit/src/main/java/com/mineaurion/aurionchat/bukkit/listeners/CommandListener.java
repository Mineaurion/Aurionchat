package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.Config;
import com.mineaurion.aurionchat.bukkit.Utils;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
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
    private final AurionChat plugin;
    private final Config config;

    public CommandListener(AurionChat plugin){
        this.plugin = plugin;
        this.config = AurionChat.config;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        Set<String> chatChannels = this.config.getAllChannel();
        Player player = event.getPlayer();
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());

        String command = event.getMessage().substring(1).split(" ")[0];
        String message = (event.getMessage().length() == command.length() + 1) ? event.getMessage().substring(command.length() + 1) : event.getMessage().substring(command.length() + 2);

        for(String channel: chatChannels){
            String channelAlias = this.config.getChannelAlias(channel);

            if(command.equalsIgnoreCase(channel) || command.equalsIgnoreCase(channelAlias)){
                if(event.getMessage().length() == command.length() + 1){
                    player.sendMessage(ChatColor.RED + "Invalid command : /" + channel + " <message>");
                    event.setCancelled(true);
                    return;
                }
                aurionChatPlayer.addChannel(channel);
                String sendFormat = Utils.processMessage(channel,message, player);

                try{
                    this.plugin.getChatService().send(channel,sendFormat);
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
