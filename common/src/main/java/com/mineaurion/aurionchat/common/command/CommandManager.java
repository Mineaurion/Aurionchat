package com.mineaurion.aurionchat.common.command;


import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.command.tabcomplete.CompletionSupplier;
import com.mineaurion.aurionchat.common.command.tabcomplete.TabCompleter;
import com.mineaurion.aurionchat.common.commands.*;
import com.mineaurion.aurionchat.common.locale.Message;
import com.mineaurion.aurionchat.common.misc.ImmutableCollectors;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandManager {

    private final AurionChatPlugin plugin;
    private final Map<String, AbstractCommand> mainCommands;

    public CommandManager(AurionChatPlugin plugin){
        this.plugin = plugin;
        this.mainCommands = ImmutableList.<AbstractCommand>builder()
                .add(new AllListen())
                .add(new JoinChannel())
                .add(new LeaveChannel())
                .add(new SpyChannel())
                .add(new Reload())
                .build()
                .stream()
                .collect(ImmutableCollectors.toMap(c -> c.getName().toLowerCase(Locale.ROOT), Function.identity()));
    }

    public AurionChatPlugin getPlugin(){
        return this.plugin;
    }

    @VisibleForTesting
    public Map<String, AbstractCommand> getMainCommands(){
        return this.mainCommands;
    }

    public void executeCommand(ServerPlayer sender, String label, List<String> args){
        UUID uuid = sender.getId();

        try {
            execute(sender, label, args);
        } catch (Throwable e){
            this.plugin.getLogger().severe("Exception whilst executing command: " + args, e);
        }
    }

    private void execute(ServerPlayer sender, String label, List<String> arguments){
        if(arguments.isEmpty() || arguments.size() == 1 && arguments.get(0).trim().isEmpty()){
            AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(sender.getId());
            Message.COMMAND_DEFAULT.send(sender, aurionChatPlayer.getCurrentChannel(), String.join(" ", aurionChatPlayer.getChannels()));
            return;
        }

        AbstractCommand main = this.mainCommands.get(arguments.get(0).toLowerCase(Locale.ROOT));

        if(main == null){
            sendCommandUsage(sender, label);
            return;
        }

        if(main.getArgumentCheck().test(arguments.size())){
            main.sendUsage(sender, label);
            return;
        }

        if(!main.beforeExecute(this.plugin, sender, arguments, label)){
            return;
        }

        //Exec command
        try{
            main.execute(this.plugin, sender, arguments, label);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> tabCompleteCommand(ServerPlayer sender, List<String> arguments){
        final  List<AbstractCommand> mains = this.mainCommands.values().stream()
                .filter(m -> m.isAuthorized(sender))
                .collect(Collectors.toList());

        return TabCompleter.create()
                .at(0, CompletionSupplier.startsWith(() -> mains.stream().map(c -> c.getName().toLowerCase(Locale.ROOT))))
                .from(1, partial -> mains.stream()
                        .filter(m -> m.getName().equalsIgnoreCase(arguments.get(0)))
                        .findFirst()
                        .map(cmd -> cmd.tabComplete(this.plugin, sender, arguments.subList(1, arguments.size())))
                        .orElse(Collections.emptyList())
                )
                .complete(arguments);
    }

    private void sendCommandUsage(ServerPlayer sender, String label) {
        sender.sendMessage(Message.prefixed(Component.text()
                .color(NamedTextColor.DARK_GREEN)
                .append(Component.text("Running "))
                .append(Component.text("Aurionchat", NamedTextColor.AQUA))
        ));

        this.mainCommands.values().stream()
                .filter(c -> c.isAuthorized(sender))
                .forEach(c -> sender.sendMessage(Component.text()
                        .append(Component.text('>', NamedTextColor.DARK_AQUA))
                        .append(Component.space())
                        .append(Component.text(String.format(c.getUsage(), label), NamedTextColor.GREEN))
                        .clickEvent(ClickEvent.suggestCommand(String.format(c.getUsage(), label)))
                        .build()
                ));
    }
}
