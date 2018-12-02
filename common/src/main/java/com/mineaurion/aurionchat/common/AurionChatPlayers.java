package com.mineaurion.aurionchat.common;

import java.util.*;

public class AurionChatPlayers {
    Map<UUID, AurionChatPlayer> aurionsChatPlayers = new HashMap<UUID, AurionChatPlayer>();

    public AurionChatPlayer getAurionsChatPlayer(UUID uuid){
        return aurionsChatPlayers.get(uuid);
    }

    public void addPlayer(UUID uuid, Set<String> channels, String currentChannel){
        AurionChatPlayer joueur = new AurionChatPlayer(channels, currentChannel);
        aurionsChatPlayers.put(uuid, joueur);
    }
    public void removePlayer(UUID uuid){
        if(aurionsChatPlayers.containsKey(uuid)){
            aurionsChatPlayers.remove(uuid);
        }
    }

    public Set<UUID> getPlayersListeningChannel(String channel){
        Set<UUID> playersListenChannel = new HashSet<>();
        aurionsChatPlayers.forEach(
                (uuid, aurionChatPlayer) -> {
                    if(aurionChatPlayer.getChannels().contains(channel)){
                        playersListenChannel.add(uuid);
                    }
                }
        );
        return playersListenChannel;
    }
}
