package com.mineaurion.aurionchat.common.commands;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.command.CommandSpec;
import com.mineaurion.aurionchat.common.command.SingleCommand;
import com.mineaurion.aurionchat.common.locale.Message;
import com.mineaurion.aurionchat.common.misc.Predicates;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;

import java.util.List;

public class LeaveChannel extends SingleCommand {

    public LeaveChannel(){
        super(CommandSpec.LEAVE, "Join", "aurionchat.command.leave", Predicates.inRange(1,2));
    }

    @Override
    public boolean beforeExecute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) {
        return basicCheckChannelCommand(plugin, sender, args.get(1));
    }

    @Override
    public void execute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) throws Exception {
        if(!sender.hasPermission(getPermission())){
            Message.COMMAND_NO_PERMISSION.send(sender);
            return;
        }
        AurionChatPlayer aurionChatPlayer = getAurionChatPlayer(plugin, sender);
        String channel = args.get(1);
        aurionChatPlayer.removeChannel(channel);
        Message.CHANNEL_LEAVE.send(sender, channel);
    }

    @Override
    public List<String> tabComplete(AurionChatPlugin plugin, ServerPlayer sender, List<String> args) {
        return basicChannelsTabComplete(plugin, sender);
    }
}
