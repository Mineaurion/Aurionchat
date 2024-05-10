package com.mineaurion.aurionchat.common.commands;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.command.CommandSpec;
import com.mineaurion.aurionchat.common.command.SingleCommand;
import com.mineaurion.aurionchat.common.locale.Message;
import com.mineaurion.aurionchat.common.misc.Predicates;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;

import java.io.IOException;
import java.util.List;

public class Reload extends SingleCommand {

    public Reload(){
        super(CommandSpec.RELOAD, "Reload", "aurionchat.command.reload", Predicates.inRange(0,1));
    }

    @Override
    public boolean beforeExecute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) {
        return true;
    }

    @Override
    public void execute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) {
        try {
            plugin.getChatService().reCreateConnection();
            Message.COMMAND_RELOAD.send(sender);
        } catch (IOException e) {
            Message.COMMAND_RELOAD_ERROR.send(sender);
            e.printStackTrace();
        }
    }
}
