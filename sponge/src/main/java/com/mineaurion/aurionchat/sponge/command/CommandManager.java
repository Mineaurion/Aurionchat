package com.mineaurion.aurionchat.sponge.command;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {
    public CommandSpec cmdChat;

    public enum Action {
        JOIN,
        LEAVE,
        SPY,
        ALLLISTEN,
    }


    public CommandManager(){
        Init();
    }

    private void Init(){

        CommandSpec cmdJoinChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(new ChatCommand( Action.JOIN))
                    .build();

        CommandSpec cmdLeaveChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(new ChatCommand(Action.LEAVE))
                    .build();

        CommandSpec cmdSpyChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(new ChatCommand(Action.SPY))
                    .build();

        CommandSpec cmdAllListenChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .executor(new ChatCommand(Action.ALLLISTEN))
                    .build();


        cmdChat = CommandSpec.builder()
                    .child(cmdJoinChat, "join", "j")
                    .child(cmdLeaveChat, "leave", "l")
                    .child(cmdSpyChat, "spy","s")
                    .child(cmdAllListenChat, "alllisten", "allspy")
                    .permission("aurionchat.command.channel")
                    .executor(new ChatDefaultCommand())
                    .build();

    }
}
