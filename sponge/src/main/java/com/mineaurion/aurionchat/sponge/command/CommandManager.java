package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.sponge.AurionChat;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {

    private AurionChat plugin;
    public CommandSpec cmdChat;


    public CommandManager(AurionChat plugin){
        this.plugin = plugin;
        Init();
    }

    private void Init(){

        CommandSpec cmdJoinChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(new ChatCommand(plugin, "join"))
                    .build();

        CommandSpec cmdLeaveChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(new ChatCommand(plugin, "leave"))
                    .build();

        CommandSpec cmdSpyChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(new ChatCommand(plugin, "spy"))
                    .build();

        CommandSpec cmdAllListenChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .executor(new ChatCommand(plugin, "alllisten"))
                    .build();


        cmdChat = CommandSpec.builder()
                    .child(cmdJoinChat, "join", "j")
                    .child(cmdLeaveChat, "leave", "l")
                    .child(cmdSpyChat, "spy","s")
                    .child(cmdAllListenChat, "alllisten", "allspy")
                    .permission("aurionchat.command.channel")
                    .executor(new ChatDefaultCommand(plugin))
                    .build();

    }
}
