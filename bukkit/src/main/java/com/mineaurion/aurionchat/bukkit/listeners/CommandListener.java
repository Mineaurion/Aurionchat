package com.mineaurion.aurionchat.bukkit.listeners;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements CommandExecutor, Listener {

    private final AurionChat aurionChat;

    public CommandListener(AurionChat aurionChat){
        this.aurionChat = aurionChat;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        String[] fullCommand = event.getMessage().split(" ");
        String command = fullCommand[0].replace("/", "");
        String message = String.join(" ",  (String[]) ArrayUtils.removeElement(fullCommand, "/" + command));
        Component messageComponent = LegacyComponentSerializer.legacy('&').deserialize(message);

        aurionChat.getConfigurationAdapter().getChannelByNameOrAlias(command).ifPresent(channelName -> {
            ChatCommandCommon.onCommand(
                    aurionChat.getAurionChatPlayers().get(event.getPlayer().getUniqueId()),
                    messageComponent,
                    channelName,
                    aurionChat.getConfigurationAdapter().getChannels().get(channelName).format
            );
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
