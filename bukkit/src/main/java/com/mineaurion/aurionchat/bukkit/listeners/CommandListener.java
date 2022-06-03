package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import com.mineaurion.aurionchat.bukkit.Config;
import com.mineaurion.aurionchat.bukkit.Utils;
import com.mineaurion.aurionchat.bukkit.channel.ChatService;
import com.mineaurion.aurionchat.common.listeners.CommandListenerCommon;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
public class CommandListener extends CommandListenerCommon<AurionChatPlayer, Utils, ChatService> implements CommandExecutor, Listener {
    private final Config config;

    public CommandListener(AurionChat plugin){
        super(AurionChat.utils, plugin.getChatService());
        this.config = AurionChat.config;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        String[] fullCommand = event.getMessage().split(" ");
        String command = fullCommand[0].replace("/", "");
        String message = String.join(" ",  (String[]) ArrayUtils.removeElement(fullCommand, "/" + command));

        AurionChat.config.getChannelByNameOrAlias(command).ifPresent(channelName -> {
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(event.getPlayer().getUniqueId());
            this.onCommand(aurionChatPlayer, message, channelName, config.channels.get(channelName).format);
            event.setCancelled(true);
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            Bukkit.getLogger().info("This command must be run by a player.");
        }
        return false;
    }

}
