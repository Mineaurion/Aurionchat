package com.mineaurion.aurionchat.sponge.command;

import com.mineaurion.aurionchat.common.command.ChatCommandCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class ChatCommand extends ChatCommandCommon {

    private final AurionChat plugin;

    public ChatCommand(AurionChat plugin, CommandManager commandManager){
        super(AurionChat.config.channels.keySet());
        this.plugin = plugin;
        register(commandManager);
        registerAliasChannels(commandManager);
    }

    private void register(CommandManager commandManager){

        CommandSpec join = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor((src, args) -> this.execute(src, args, Action.JOIN))
                    .build();

        CommandSpec leave = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor((src, args) -> this.execute(src, args, Action.LEAVE))
                    .build();

        CommandSpec spy = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("channel"))))
                    .executor(((src, args) -> this.execute(src, args, Action.SPY)))
                    .build();

        CommandSpec allListen = CommandSpec.builder()
                    .permission("aurionchat.command.channel")
                    .description(Text.of("AurionChat's command to manage chat channels"))
                    .executor((src, args) -> this.execute(src ,args, Action.ALLLISTEN))
                    .build();

        CommandSpec reload = CommandSpec.builder()
                .permission("aurionchat.reload")
                .description(Text.of("Aurionchat reload command"))
                .executor(((src, args) -> this.execute(src, args, Action.RELOAD)))
                .build();

        CommandSpec cmdChat = CommandSpec.builder()
                    .child(join, "join")
                    .child(leave, "leave")
                    .child(spy, "spy")
                    .child(allListen, "alllisten")
                    .child(reload, "reload")
                    .executor((src, args) -> this.execute(src, args, Action.DEFAULT))
                    .build();

        commandManager.register(this.plugin, cmdChat,"channel", "ch");
    }

    private void registerAliasChannels(CommandManager commandManager){
        AurionChat.config.channels.forEach((name, channel) -> {
            CommandSpec aliasChannels = CommandSpec.builder()
                    .arguments(GenericArguments.remainingJoinedStrings(Text.of("message")))
                    .executor((src, args) -> (onCommand(
                            this.plugin.getAurionChatPlayers().get(((Player) src).getUniqueId()),
                            args.<String>getOne("message").orElse(""),
                            name,
                            channel.format
                    )) ? CommandResult.success() : CommandResult.empty())
                    .build();
            commandManager.register(
                    this.plugin,
                    aliasChannels,
                    channel.alias, name
            );
        });
    }

    public CommandResult execute(CommandSource src, CommandContext args, Action action) {
        if(!(src instanceof Player)){
            return CommandResult.empty();
        } else {
            AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(((Player) src).getUniqueId());
            String channel = args.<String>getOne("channel").orElse("");
            return this.execute(aurionChatPlayer, channel, action) ? CommandResult.success() : CommandResult.empty();
        }
    }
}
