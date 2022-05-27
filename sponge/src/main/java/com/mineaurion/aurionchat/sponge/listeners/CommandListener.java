package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Set;

public class CommandListener {

    private final AurionChat plugin;
    private final Config config;

    public CommandListener(AurionChat plugin){
        this.plugin = plugin;
        this.config = AurionChat.config;
    }

    @Listener
    public void onCommand(SendCommandEvent event, @First Player player){
        Set<String> chatChannels = config.channels.keySet();
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());

        String command = event.getCommand();
        String message = event.getArguments();

        for(String channel:chatChannels){
            String channelAlias = config.channels.get(channel).alias;

            if(command.equalsIgnoreCase(channel) || command.equalsIgnoreCase(channelAlias)){
                if(event.getArguments().length() == 0){
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&4Invalid command : /" + command + "<message>"));
                    event.setCancelled(true);
                    return;
                }
                aurionChatPlayer.addChannel(channel);
                String sendFormat = AurionChat.utils.processMessage(channel, message, aurionChatPlayer);

                try{
                    plugin.getChatService().send(channel, sendFormat);
                }
                catch(Exception e){
                    AurionChat.logger.error(e.getMessage());
                }
                event.setCancelled(true);
            }
        }
    }

}
