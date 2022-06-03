package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class ChatCommand extends ChatCommandCommon<AurionChatPlayer> {
    public CommandSpec cmdChat;


    public ChatCommand(){
        super(AurionChat.config.channels.keySet());
        Init();
    }

    private void Init(){

        CommandSpec cmdJoinChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor((src, args) -> this.execute(src, args, Action.JOIN))
                    .build();

        CommandSpec cmdLeaveChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor((src, args) -> this.execute(src, args, Action.LEAVE))
                    .build();

        CommandSpec cmdSpyChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(((src, args) -> this.execute(src, args, Action.SPY)))
                    .build();

        CommandSpec cmdAllListenChat = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .executor((src, args) -> this.execute(src ,args, Action.ALLLISTEN))
                    .build();


        cmdChat = CommandSpec.builder()
                    .child(cmdJoinChat, "join", "j")
                    .child(cmdLeaveChat, "leave", "l")
                    .child(cmdSpyChat, "spy","s")
                    .child(cmdAllListenChat, "alllisten", "allspy")
                    .permission("aurionchat.command.channel")
                    .executor((src, args) -> this.execute(src, args, Action.DEFAULT))
                    .build();

    }

    public CommandResult execute(CommandSource src, CommandContext args, Action action) {
        if(!(src instanceof Player)){
            return CommandResult.empty();
        } else {
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(((Player) src).getUniqueId());
            String channel = args.<String>getOne("channel").orElse("");
            return this.execute(aurionChatPlayer, channel, action) ? CommandResult.success() : CommandResult.empty();
        }
    }
}
