package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.listeners.CommandListenerCommon;
import com.mineaurion.aurionchat.forge.AurionChat;
import com.mineaurion.aurionchat.forge.AurionChatPlayer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;

public class CommandListener extends CommandListenerCommon<AurionChatPlayer> {

    private final AurionChat plugin;

    public CommandListener(AurionChat plugin)
    {
        this.plugin = plugin;
    }

    @SubscribeEvent
    public void onCommandEvent(CommandEvent event){
        String[] fullCommand = event.getParseResults().getReader().getString().split(" ");
        String command = fullCommand[0].replace("/", "");
        String message = String.join(" ", ArrayUtils.removeElement(fullCommand, "/" + command)) ;
        AurionChat.config.getChannelByNameOrAlias(command).ifPresent(channelName -> {
          try {
              AurionChatPlayer aurionChatPlayer = plugin.getAurionChatPlayers().get(
                      event.getParseResults().getContext().getSource().getPlayerOrException().getUUID()
              );
              this.onCommand(aurionChatPlayer, message, channelName, AurionChat.config.getChannels().get(channelName).format);
              event.setResult(Event.Result.ALLOW);
              event.setCanceled(true);

          } catch (CommandSyntaxException e){
              LogManager.getLogger().info("This command must be run by a player");
          }
        });
    }
}
