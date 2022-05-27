package com.mineaurion.aurionchat.bukkit.command;

import com.mineaurion.aurionchat.bukkit.AurionChat;
import com.mineaurion.aurionchat.bukkit.AurionChatPlayer;
import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChatCommand extends ChatCommandCommon<AurionChatPlayer> implements CommandExecutor {
    public ChatCommand() {
        super(AurionChat.config.getAllChannel());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(Bukkit.getPlayer(sender.getName()).getUniqueId());
        if(cmd.getName().equalsIgnoreCase("chat") || cmd.getName().equalsIgnoreCase("ch")){
            String command = (args.length < 1) ? "Default" : args[0];
            String channel = args.length == 2 ? args[1] : "";
            return this.execute(aurionChatPlayer, channel, Action.valueOf(command.toUpperCase()));
        }
        return false;
    }
}
