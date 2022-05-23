package com.mineaurion.aurionchat.listeners;

import com.mineaurion.aurionchat.AurionChat;
import com.mineaurion.aurionchat.AurionChatPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class LoginListener {

    private final Map<UUID, AurionChatPlayer> aurionChatPlayers;

    public LoginListener(){
        this.aurionChatPlayers = AurionChat.aurionChatPlayers;
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        String defaultChannel = AurionChat.config.rabbitmq.serverName.get();
        Set<String> defaultChannels = new HashSet<>(Arrays.asList("global", defaultChannel));
        // if the user is op then he will listen to all the channel
        if(event.getPlayer().hasPermissions(2)){
            AurionChat.config.getChannels().forEach((name, channel) -> defaultChannels.add(name));
        }
        AurionChatPlayer aurionChatPlayer = new AurionChatPlayer(defaultChannels, defaultChannel);
        this.aurionChatPlayers.putIfAbsent(event.getPlayer().getUUID(), aurionChatPlayer);
    }

    @SubscribeEvent
    public void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event){
        this.aurionChatPlayers.remove(event.getPlayer().getUUID());
    }
}
