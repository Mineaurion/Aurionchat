package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.AurionChatPlayers;
import com.mineaurion.aurionchat.sponge.AurionChat;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;

import java.util.UUID;

public class ChatListener {

    private AurionChat plugin;
    private AurionChatPlayers aurionChatPlayers;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
        this.aurionChatPlayers = plugin.getAurionChatPlayers();
    }

    @Listener @IsCancelled(Tristate.UNDEFINED)
    public void onPlayerChat(MessageChannelEvent.Chat event, @First Player player){
        if(event.isCancelled()) {
            return;
        }
        UUID uuid = player.getUniqueId();
        AurionChatPlayer aurionChatPlayer = aurionChatPlayers.getAurionsChatPlayer(uuid);

        Text evMessage = event.getRawMessage();
        String evChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = plugin.getUtils().processMessage(evChannel, evMessage, player);

        try{
            plugin.getChatService().send(evChannel, messageFormat);
        }
        catch (Exception e){
            plugin.getLogger().error(e.getMessage());
        }
        event.setCancelled(true);
    }
}
