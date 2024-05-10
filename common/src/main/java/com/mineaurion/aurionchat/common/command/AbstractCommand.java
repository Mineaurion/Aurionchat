package com.mineaurion.aurionchat.common.command;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractCommand {

    private final @NonNull CommandSpec spec;
    private final @NonNull String name;

    private final @NonNull String permission;

    private final @NonNull Predicate<Integer> argumentCheck;

    public AbstractCommand(@NonNull CommandSpec spec, @NonNull String name, @NonNull String permission, @NonNull Predicate<Integer> argumentCheck){
        this.spec = spec;
        this.name = name;
        this.permission = permission;
        this.argumentCheck = argumentCheck;
    }

    // Method before execute method, to make some check and eventually cancel the execute method when the method return false
    public abstract boolean beforeExecute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label);

    // Main execution method for the command.
    public abstract void execute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) throws Exception;


    // Tab completion method - default implementation provided since some commands do not provide more tab completion.
    public List<String> tabComplete(AurionChatPlugin plugin, ServerPlayer sender, List<String> args) {
        return Collections.emptyList();
    }

    public @NonNull CommandSpec getSpec(){
        return this.spec;
    }

    public @NonNull String getName() {
        return this.name;
    }

    public String getUsage(){
        String usage = getSpec().usage();
        return usage == null ? "" : usage;
    }

    public @NotNull String getPermission(){
        return permission;
    }

    public Optional<List<Argument>> getArgs(){
        return Optional.ofNullable(getSpec().args());
    }

    public @NonNull Predicate<Integer> getArgumentCheck(){
        return this.argumentCheck;
    }

    /**
     * The admin permission in untreated here, directly handle in the command class for less complication
     * @return boolean true if the sender has permissions
     */
    public boolean isAuthorized(ServerPlayer sender){
        return sender.hasPermission(getPermission());
    }

    /**
     * Sends a brief command usage message to the Sender.
     */
    public abstract void sendUsage(ServerPlayer sender, String label);
}
