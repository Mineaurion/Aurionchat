package com.mineaurion.aurionchat.bukkit.command;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand extends ChatCommandCommon<AurionChatPlayer> implements CommandExecutor {
    public ChatCommand() {
        super(AurionChat.config.channels.keySet());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("chat") || cmd.getName().equalsIgnoreCase("ch")){
            if(!(sender instanceof Player)){
                return false;
            }
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(Bukkit.getPlayer(sender.getName()).getUniqueId());
            String command = args.length < 1 ? "DEFAULT" : args[0];
            String channel = (args.length == 2) ? args[1] : "";


            try {
                Action action;
                if(channel.equalsIgnoreCase("alllisten")){
                    action = Action.ALLLISTEN;
                } else {
                    action = Action.valueOf(command.toUpperCase());
                }
                return this.execute(aurionChatPlayer, channel, action);
            } catch (IllegalArgumentException e){
                aurionChatPlayer.sendMessage("&6This command doesn't exist");
                return false;
            }
        }
        return false;
    }
}
