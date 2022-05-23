package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.sponge.AurionChat;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import static com.mineaurion.aurionchat.sponge.Utils.sendConsoleMessage;

public class ChatDefaultCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());
            StringBuilder message = new StringBuilder();
            StringBuilder channels = new StringBuilder();
            StringBuilder avalaibleChannels = new StringBuilder();

            for(String channel: aurionChatPlayer.getChannels()){
                channels.append(channel).append(" ");
            }
            for(String avalaibleChannel: AurionChat.config.channels.keySet()){
                avalaibleChannels.append(avalaibleChannel).append(" ");
            }
            message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                    .append("&7Spying on channels:&f ").append(channels)
                    .append("&7 Avalaible channel:&f ").append(avalaibleChannels);
            player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message.toString()) );
        }
        else{
           sendConsoleMessage("Console can listen all channel if you want, check config");
        }
        return CommandResult.success();
    }
}
