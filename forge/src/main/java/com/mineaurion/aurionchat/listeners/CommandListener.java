package com.mineaurion.aurionchat.listeners;

import com.mineaurion.aurionchat.AurionChat;
import com.mineaurion.aurionchat.AurionChatPlayer;
import com.mineaurion.aurionchat.Utils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

public class CommandListener {

    private AurionChat plugin;
    public CommandListener(AurionChat plugin)
    {
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onCommandEvent(CommandEvent event) throws CommandSyntaxException {
        ServerPlayerEntity player = event.getParseResults().getContext().getSource().getPlayerOrException();
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUUID());

        String[] fullCommand = event.getParseResults().getReader().getString().split(" ");
        String command = fullCommand[0].replace("/", "");
        String message = String.join(" ", ArrayUtils.removeElement(fullCommand, "/" + command)) ;

        AurionChat.config.getChannels().forEach( (name, channel) -> {
            if(command.equalsIgnoreCase(name) ||command.equalsIgnoreCase(channel.alias)){
                if(message.length() == 0){
                    event.setResult(Event.Result.DENY);
                }
                aurionChatPlayer.addChannel(name);
                String messageFormat = Utils.processMessage(name, message, player);
                try {
                    this.plugin.getChatService().send(name, messageFormat);
                } catch (IOException e) {
                    LogManager.getLogger().error(e.getMessage());
                }
                event.setResult(Event.Result.ALLOW);
                event.setCanceled(true);
            }
        });
    }
}
