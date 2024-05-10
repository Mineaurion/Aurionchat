package com.mineaurion.aurionchat.common.command;

import com.mineaurion.aurionchat.api.model.ServerPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.common.locale.Message;
import com.mineaurion.aurionchat.common.plugin.AurionChatPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mineaurion.aurionchat.common.command.CommandSpec.PERMISSION_BY_CHANNEL;

public abstract class SingleCommand extends AbstractCommand {

    public SingleCommand(CommandSpec spec, String name, String permission, Predicate<Integer> argumentCheck){
        super(spec, name, permission, argumentCheck);
    }

    public abstract void execute(AurionChatPlugin plugin, ServerPlayer sender, List<String> args, String label) throws Exception;

    @Override
    public void sendUsage(ServerPlayer sender, String label) {
        TextComponent.Builder builder = Component.text()
                .append(Component.text('>', NamedTextColor.DARK_AQUA))
                .append(Component.space())
                .append(Component.text(getName().toLowerCase(Locale.ROOT), NamedTextColor.GREEN));

        if(getArgs().isPresent()){
            List<Component> argUsages = getArgs().get().stream()
                    .map(Argument::asComponent)
                    .collect(Collectors.toList());

            builder.append(Component.text(" - ", NamedTextColor.DARK_AQUA))
                    .append(Component.join(JoinConfiguration.separator(Component.space()), argUsages));
        }
        sender.sendMessage(builder.build());
    }

    public AurionChatPlayer getAurionChatPlayer(AurionChatPlugin plugin, ServerPlayer sender){
        return plugin.getAurionChatPlayers().get(sender.getId());
    }

    ;

    public boolean basicCheckChannelCommand(AurionChatPlugin plugin, ServerPlayer sender, String channel){
        boolean isValid = true;
        if(!plugin.getConfigurationAdapter().getChannels().containsKey(channel)){
            isValid = false;
            Message.CHANNEL_NOT_FOUND.send(sender, channel);
        }

        if(getAurionChatPlayer(plugin, sender) == null){
            isValid = false;
            Message.PLAYER_NOT_REGISTERED.send(sender);
        }

        if(!sender.hasPermission(String.format(PERMISSION_BY_CHANNEL, getName().toLowerCase(), channel))){
            isValid = false;
            Message.COMMAND_NO_PERMISSION.send(sender);
        }
        return isValid;
    }

    public List<String> basicChannelsTabComplete(AurionChatPlugin plugin, ServerPlayer sender) {
        Map<String, Channel> channels = plugin.getConfigurationAdapter().getChannels();
        List<String> tabComplete = new ArrayList<>();
        channels.forEach((channelName, channel) -> {
            if(sender.hasPermission(String.format(PERMISSION_BY_CHANNEL, getName().toLowerCase(), channelName))){
                tabComplete.add(channelName);
            }
        });
        return tabComplete;
    }

}
