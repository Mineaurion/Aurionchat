package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.listeners.CommandListenerCommon;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import com.mineaurion.aurionchat.sponge.Utils;
import com.mineaurion.aurionchat.sponge.channel.ChatService;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;

public class CommandListener extends CommandListenerCommon<AurionChatPlayer, Utils, ChatService> {

    public CommandListener(AurionChat plugin){
        super(AurionChat.utils, plugin.getChatService());
    }

    @Listener
    public void onCommand(SendCommandEvent event, @First Player player){
        String command = event.getCommand();
        String message = event.getArguments();

        AurionChat.config.getChannelByNameOrAlias(command).ifPresent(channelname -> {
            AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());
            this.onCommand(aurionChatPlayer, message, channelname, AurionChat.config.channels.get(channelname).format);
            event.setResult(CommandResult.success());
            event.setCancelled(true);
        });
    }

}
