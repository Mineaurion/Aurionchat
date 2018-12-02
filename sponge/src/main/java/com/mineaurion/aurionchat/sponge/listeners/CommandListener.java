package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Set;

public class CommandListener {

    private AurionChat plugin;
    private Config config;
    private AurionChatPlayers aurionChatPlayers;

    public CommandListener(AurionChat plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    @Listener
    public void onCommand(SendCommandEvent event, @First Player player){
        Set<String> chatChannels = config.channels.keySet();
        AurionChatPlayer aurionChatPlayer = aurionChatPlayers.getAurionsChatPlayer(player.getUniqueId());

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
                aurionChatPlayer.addListening(channel);
                String sendFormat = plugin.getUtils().processMessage(channel, TextSerializers.FORMATTING_CODE.deserialize(message), player);

                try{
                    plugin.getChatService().send(channel, sendFormat);
                }
                catch(Exception e){
                    plugin.getLogger().error(e.getMessage());
                }
                event.setCancelled(true);
            }
        }
    }

}
