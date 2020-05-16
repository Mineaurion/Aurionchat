package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.sponge.AurionChat;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.UUID;

public class ChatDefaultCommand implements CommandExecutor {

    private AurionChat plugin;
    private AurionChatPlayers aurionChatPlayers;

    public ChatDefaultCommand(AurionChat plugin){
        this.plugin = plugin;
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            UUID uuid = player.getUniqueId();
            AurionChatPlayer aurionChatPlayer = aurionChatPlayers.getAurionChatPlayer(uuid);
            StringBuilder message = new StringBuilder();
            StringBuilder channels = new StringBuilder();
            StringBuilder avalaibleChannels = new StringBuilder();

            for(String channel:aurionChatPlayer.getChannels()){
                channels.append(channel).append(" ");
            }
            for(String avalaibleChannel: plugin.getConfig().channels.keySet()){
                avalaibleChannels.append(avalaibleChannel);
            }
            message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                    .append("&7Spying on channels:&f ").append(channels.toString())
                    .append("&7 Avalaible channel:&f ").append(avalaibleChannels.toString());
            player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message.toString()) );
        }
        else{
            plugin.sendConsoleMessage("Console can listen all channel if you want, check config");
        }
        return CommandResult.success();
    }
}
