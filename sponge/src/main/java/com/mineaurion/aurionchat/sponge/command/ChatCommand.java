package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Set;
import java.util.UUID;

import static com.mineaurion.aurionchat.sponge.Utils.sendConsoleMessage;

public class ChatCommand implements CommandExecutor {
    private final CommandManager.Action action;
    private final Config config;

    public ChatCommand(CommandManager.Action action){
        this.action = action;
        this.config = AurionChat.config;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if(src instanceof Player){
            Player player = (Player) src;
            UUID uuid = ((Player) src).getUniqueId();
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(uuid);
            String channel = args.<String>getOne("channel").orElse("");
            if(this.action == CommandManager.Action.ALLLISTEN){
                ChatAllListen(player, aurionChatPlayer);
            }
            else{
                ChatAction(player, aurionChatPlayer, channel, this.action);
            }
        }
        else{
            sendConsoleMessage("Console can listen all channel if you want, check config");
        }
        return CommandResult.success();
    }

    private void ChatAction(Player player, AurionChatPlayer aurionChatPlayer, String channel, CommandManager.Action action){
        Set<String> channels = config.channels.keySet();
        if(channels.contains(channel)){
            switch(action){
                case JOIN:
                    aurionChatPlayer.removeChannel(aurionChatPlayer.getCurrentChannel());
                    aurionChatPlayer.setCurrentChannel(channel);
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize( "&cYou have joined the " + channel + " channel."));
                    break;
                case LEAVE:
                    aurionChatPlayer.removeChannel(channel);
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6You have leaved the " + channel + " channel."));
                    break;
                case SPY:
                    aurionChatPlayer.addChannel(channel);
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6You have spy the " + channel + " channel."));
                    break;
            }
        }
        else{
            player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&6This channel doesn't exist"));
        }
    }

    private void ChatAllListen(Player player, AurionChatPlayer aurionChatPlayer){
        for(String channel: config.channels.keySet()){
            ChatAction(player, aurionChatPlayer, channel, CommandManager.Action.SPY);
        }
    }

}
