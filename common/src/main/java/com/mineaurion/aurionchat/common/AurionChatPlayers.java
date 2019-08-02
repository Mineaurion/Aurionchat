package com.mineaurion.aurionchat.common;

import java.util.*;

public class AurionChatPlayers {
    Map<UUID, AurionChatPlayer> aurionChatPlayers = new HashMap<UUID, AurionChatPlayer>();

    public AurionChatPlayer getAurionChatPlayer(UUID uuid){
        return aurionChatPlayers.get(uuid);
    }

    public void addPlayer(UUID uuid, Set<String> channels, String currentChannel){
        AurionChatPlayer joueur = new AurionChatPlayer(channels, currentChannel);
        aurionChatPlayers.put(uuid, joueur);
    }
    public void removePlayer(UUID uuid){
        if(aurionChatPlayers.containsKey(uuid)){
            aurionChatPlayers.remove(uuid);
        }
    }

    public Set<UUID> getPlayersListeningChannel(String channel){
        Set<UUID> playersListenChannel = new HashSet<>();
        aurionChatPlayers.forEach(
                (uuid, aurionChatPlayer) -> {
                    if(aurionChatPlayer.getChannels().contains(channel)){
                        playersListenChannel.add(uuid);
                    }
                }
        );
        return playersListenChannel;
    }
}
