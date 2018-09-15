package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

public class ChatDefaultCommand implements CommandExecutor {

    private AurionChat plugin;

    public ChatDefaultCommand(AurionChat plugin){
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(src.getName());
        if(src instanceof Player){
            StringBuilder message = new StringBuilder();
            StringBuilder channels = new StringBuilder();

            for(String channel:aurionChatPlayer.getListening()){
                channels.append(channel).append(" ");
            }
            message.append("&7Your current channel:&f ").append(aurionChatPlayer.getCurrentChannel()).append("\n")
                    .append("&7Spying on channels:&f ").append(channels.toString());

            aurionChatPlayer.getPlayer().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message.toString()) );
        }
        else{
            plugin.sendConsoleMessage("Console can listen all channel if you want, check config");
        }
        return CommandResult.success();
    }
}
