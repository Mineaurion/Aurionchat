package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Set;

public class CommandListener {

    private AurionChat plugin;

    public CommandListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @Listener
    public void onCommand(SendCommandEvent event, @First Player player){
        Set<String> chatChannels = plugin.getConfig().getAllChannel();
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getAurionChatPlayer(player);

        String command = event.getCommand();
        String message = event.getArguments();

        for(String channel:chatChannels){
            String channelAlias = plugin.getConfig().getChannelAlias(channel);

            if(command.equalsIgnoreCase(channel) || command.equalsIgnoreCase(channelAlias)){
                if(event.getArguments().length() == 0){
                    aurionChatPlayer.getPlayer().sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&4Invalid command : /" + command + "<message>"));
                    event.setCancelled(true);
                    return;
                }
                aurionChatPlayer.addListening(channel);
                String sendFormat = plugin.getUtils().processMessage(channel, TextSerializers.FORMATTING_CODE.deserialize(message), aurionChatPlayer.getPlayer());

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
