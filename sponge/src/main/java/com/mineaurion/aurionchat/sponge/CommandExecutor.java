package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.command.CommandManager;
import com.mineaurion.aurionchat.common.misc.ArgumentTokenizer;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.ArgumentReader;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandExecutor extends CommandManager implements Command.Raw {
    private final AurionChat plugin;

    public CommandExecutor(AurionChat plugin){
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public CommandResult process(CommandCause source, ArgumentReader.Mutable arguments) throws CommandException {
        //TODO: warning workaround maybe not ok for passing arguments to executeCommand
        executeCommand(
                this.plugin.getSenderFactory().wrap(source.audience()),
                "aurionchat",
                Arrays.asList(arguments.input().split(" "))
        );
        return CommandResult.success();
    }

    @Override
    public List<CommandCompletion> complete(CommandCause source, ArgumentReader.Mutable arguments) throws CommandException {
        return tabCompleteCommand(
                plugin.getSenderFactory().wrap(source.audience()),
                ArgumentTokenizer.TAB_COMPLETE.tokenizeInput(arguments.input())
        ).stream()
                .map(CommandCompletion::of)
                .collect(Collectors.toList())
                ;
    }

    @Override
    public boolean canExecute(CommandCause cause) {
        return true; // check permission internally
    }

    @Override
    public Optional<Component> shortDescription(CommandCause cause) {
        return Optional.of(Component.text("Use & Manage Aurionchat"));
    }

    @Override
    public Optional<Component> extendedDescription(CommandCause cause) {
        return Optional.empty();
    }

    @Override
    public Component usage(CommandCause cause) {
        return Component.text("/chat");
    }
}