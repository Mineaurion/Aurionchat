package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Set;

public class ChatCommand implements CommandExecutor {

    private AurionChat plugin;
    private String action;

    public ChatCommand(AurionChat plugin, String action){
        this.plugin = plugin;
        this.action = action;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(src.getName());
        if(src instanceof Player){
            String channel = args.<String>getOne("channel").orElse("");
            plugin.sendConsoleMessage("Super channel : " + channel);
            if(this.action.equalsIgnoreCase("alllisten")){
                ChatAllListen(aurionChatPlayer);
            }
            else{
                ChatAction(aurionChatPlayer, channel, this.action);
            }
        }
        else{
            plugin.sendConsoleMessage("Console can listen all channel if you want, check config");
        }
        return CommandResult.success();
    }

    private void ChatAction(AurionChatPlayer aurionChatPlayer, String channel, String action){
        Set<String> channels = plugin.getConfig().getAllChannel();
        if(channels.contains(channel)){
            switch(action){
                case "join":
                    aurionChatPlayer.removeListening(aurionChatPlayer.getCurrentChannel());
                    aurionChatPlayer.setCurrentChannel(channel);
                    aurionChatPlayer.getPlayer().sendMessage(TextSerializers.FORMATTING_CODE.deserialize( "&cYou have joined the " + channel + " channel."));
                    break;
                case "leave":
                    aurionChatPlayer.removeListening(channel);
                    aurionChatPlayer.getPlayer().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6You have leaved the " + channel + " channel."));
                    break;
                case "spy":
                    aurionChatPlayer.addListening(channel);
                    aurionChatPlayer.getPlayer().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6You have spy the " + channel + " channel."));
                    break;
            }
        }
        else{
            aurionChatPlayer.getPlayer().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6This channel doesn't exist"));
        }
    }

    private void ChatAllListen(AurionChatPlayer aurionChatPlayer){
        Set<String> channels = plugin.getConfig().getAllChannel();
        for(String channel:channels){
            ChatAction(aurionChatPlayer, channel, "spy");
        }
    }

}
