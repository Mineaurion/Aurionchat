package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.util.Tristate;

public class ChatListener {

    private final AurionChat plugin;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @Listener @IsCancelled(Tristate.UNDEFINED)
    public void onPlayerChat(MessageChannelEvent.Chat event, @First Player player){
        if(event.isCancelled()) {
            return;
        }
        if(!player.hasPermission("aurionchat.chat.speak")){
            event.setCancelled(true);
            return;
        }
        AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(player.getUniqueId());

        String currentChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = AurionChat.utils.processMessage(
                AurionChat.config.channels.get(currentChannel).format,
                event.getRawMessage().toPlain(),
                aurionChatPlayer
        );

        try{
            plugin.getChatService().send(currentChannel, messageFormat);
        }
        catch (Exception e){
           AurionChat.logger.error(e.getMessage());
        }
        event.setCancelled(true);  // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}
