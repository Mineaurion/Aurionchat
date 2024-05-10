package com.mineaurion.aurionchat.common.commands;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.command.CommandSpec;
import com.mineaurion.aurionchat.common.command.SingleCommand;
import com.mineaurion.aurionchat.common.locale.Message;
import com.mineaurion.aurionchat.common.misc.Predicates;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;

import java.util.List;

import static com.mineaurion.aurionchat.common.command.CommandSpec.PERMISSION_BY_CHANNEL;

public class AllListen extends SingleCommand {

    public AllListen(){
        super(CommandSpec.ALL_LISTEN, "AllListen", "aurionchat.command.alllisten", Predicates.inRange(0,1));
    }

    @Override
    public boolean beforeExecute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) {
        AurionChatPlayer aurionChatPlayer = getAurionChatPlayer(plugin, sender);
        if(aurionChatPlayer == null){
            Message.PLAYER_NOT_REGISTERED.send(sender);
            return false;
        }
        return true;
    }

    @Override
    public void execute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) {
        AurionChatPlayer aurionChatPlayer = getAurionChatPlayer(plugin, sender);
        plugin.getConfigurationAdapter().getChannels().forEach((channelName, channel) -> {
            if(sender.hasPermission(String.format(PERMISSION_BY_CHANNEL, getName().toLowerCase(), channelName))){
               aurionChatPlayer.addChannel(channelName);
               Message.CHANNEL_SPY.send(sender, channelName);
            }
        });
    }
}
