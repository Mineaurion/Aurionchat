package com.mineaurion.aurionchat.sponge.listeners;

import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.AurionChatPlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class ChatListener {

    private AurionChat plugin;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @Listener
    public void onPlayerChat(MessageChannelEvent.Chat event, @First Player player){
        if(event.isCancelled()) {
            return;
        }
        AurionChatPlayer aurionChatPlayer = AurionChatPlayer.getOnlineAurionChatPlayer(player);
        String spy = "";

        if(aurionChatPlayer.getPlayer().hasPermission("aurionchat.spy.override")){
            for(AurionChatPlayer p: AurionChat.onlinePlayers){
                if(p.isOnline() && p.isSpy()){
                    p.getPlayer().sendMessage(TextSerializers.PLAIN.deserialize(spy));
                }
            }
        }
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
        return;
    }
}
