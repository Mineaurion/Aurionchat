package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Set;
import java.util.UUID;

public class ChatCommand implements CommandExecutor {

    private AurionChat plugin;
    private String action;
    private Config config;
    private AurionChatPlayers aurionChatPlayers;

    public ChatCommand(AurionChat plugin, String action){
        this.plugin = plugin;
        this.action = action;
        this.config = plugin.getConfig();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            UUID uuid = ((Player) src).getUniqueId();
            AurionChatPlayer aurionChatPlayer = aurionChatPlayers.getAurionsChatPlayer(uuid);
            String channel = args.<String>getOne("channel").orElse("");
            if(this.action.equalsIgnoreCase("alllisten")){
                ChatAllListen(player, aurionChatPlayer);
            }
            else{
                ChatAction(player, aurionChatPlayer, channel, this.action);
            }
        }
        else{
            plugin.sendConsoleMessage("Console can listen all channel if you want, check config");
        }
        return CommandResult.success();
    }

    private void ChatAction(Player player, AurionChatPlayer aurionChatPlayer, String channel, String action){
        Set<String> channels = config.channels.keySet();
        if(channels.contains(channel)){
            switch(action){
                case "join":
                    aurionChatPlayer.removeListening(aurionChatPlayer.getCurrentChannel());
                    aurionChatPlayer.setCurrentChannel(channel);
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize( "&cYou have joined the " + channel + " channel."));
                    break;
                case "leave":
                    aurionChatPlayer.removeListening(channel);
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6You have leaved the " + channel + " channel."));
                    break;
                case "spy":
                    aurionChatPlayer.addListening(channel);
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6You have spy the " + channel + " channel."));
                    break;
            }
        }
        else{
            player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6This channel doesn't exist"));
        }
    }

    private void ChatAllListen(Player player, AurionChatPlayer aurionChatPlayer){
        Set<String> channels = config.channels.keySet();
        for(String channel:channels){
            ChatAction(player, aurionChatPlayer, channel, "spy");
        }
    }

}
