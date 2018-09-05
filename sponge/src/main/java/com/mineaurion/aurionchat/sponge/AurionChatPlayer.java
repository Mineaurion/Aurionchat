package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.AurionChatPlayerCommon;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class AurionChatPlayer extends AurionChatPlayerCommon {

    private AurionChat plugin;
    private Optional<Player> player;


    public AurionChatPlayer(UUID uuid, String name, String currentChannel, Set<String> listening, String nickname, boolean spy){
        super(uuid, name, currentChannel, listening, nickname, spy);
    }

    @Override
    public void setOnline(boolean online){
        this.online = online;
        if(this.online) {
            this.player = Sponge.getServer().getPlayer(this.uuid);
        }
    }

    public Optional<Player> getPlayer(){
        return this.online ? this.player : null;
    }

    public static AurionChatPlayer getAurionChatPlayer(Player player){
        for(AurionChatPlayer aurionChatPlayer: AurionChat.players){
            if(aurionChatPlayer.getUuid().toString().equals(player.getUniqueId().toString())){
                return aurionChatPlayer;
            }
        }
        return null;
    }

    public static AurionChatPlayer getAurionChatPlayer(UUID uuid){
        for( AurionChatPlayer aurionChatPlayer: AurionChat.players){
            if(aurionChatPlayer.getUuid().toString().equals(uuid.toString())){
                return aurionChatPlayer;
            }
        }
        return null;
    }

    public static AurionChatPlayer getAurionChatPlayer(String name){
        for( AurionChatPlayer aurionChatPlayer: AurionChat.players){
            if(aurionChatPlayer.getName().equalsIgnoreCase(name)){
                return aurionChatPlayer;
            }
        }
        return null;
    }

    public static AurionChatPlayer getOnlineAurionChatPlayer(Player player){
        for (AurionChatPlayer aurionChatPlayer: AurionChat.onlinePlayers){
            if(aurionChatPlayer.getUuid().toString().equals(player.getUniqueId().toString())){
                return aurionChatPlayer;
            }
        }
        return null;
    }

    public static AurionChatPlayer getOnlineAurionChatPlayer(UUID uuid){
        for (AurionChatPlayer aurionChatPlayer: AurionChat.onlinePlayers){
            if(aurionChatPlayer.getUuid().toString().equals(uuid.toString())){
                return aurionChatPlayer;
            }
        }
        return null;
    }

    public static AurionChatPlayer getOnlineAurionChatPlayer(String name){
        for (AurionChatPlayer aurionChatPlayer: AurionChat.onlinePlayers){
            if(aurionChatPlayer.getName().equalsIgnoreCase(name)){
                return aurionChatPlayer;
            }
        }
        return null;
    }
}
