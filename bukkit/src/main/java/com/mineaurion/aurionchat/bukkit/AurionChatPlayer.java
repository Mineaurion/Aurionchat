package com.mineaurion.aurionchat.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import com.mineaurion.aurionchat.bukkit.AurionChat;

public class AurionChatPlayer {

    private UUID uuid;
    private String name;
    private String currentChannel;
    private Set<String> listening;
    private String nickname;
    private boolean online;
    private Player player;
    private boolean spy;


    public AurionChatPlayer(UUID uuid, String name, String currentChannel, Set<String> listening, String nickname, boolean spy){
        this.uuid = uuid;
        this.name = name;
        this.currentChannel = currentChannel;
        this.listening = listening;
        this.nickname = nickname;
        this.spy = spy;
    }

    public UUID getUuid(){
        return this.uuid;
    }
    public String getName(){
        return this.name;
    }

    public String getCurrentChannel(){
        return this.currentChannel;
    }

    public boolean setCurrentChannel(String channel){
        if(channel != null){
            this.currentChannel = channel;
            return true;
        }
        return false;
    }

    public Set<String> getListening(){
        return this.listening;
    }

    public boolean addListening(String channel){
        if(channel != null){
            this.listening.add(channel);
            return true;
        }
        return false;
    }

    public boolean removeListening(String channel){
        if(channel != null){
            this.listening.remove(channel);
            return true;
        }
        return false;
    }

    public void clearListening(){
        this.listening.clear();
    }

    public boolean isOnline(){
        return this.online;
    }

    public void setOnline(boolean online){
        this.online = online;
        if(this.online) {
            this.player = Bukkit.getPlayer(this.uuid);
        }
        else{
            this.player = null;
        }
    }

    public Player getPlayer(){
        return this.online ? this.player : null;
    }

    public boolean isSpy(){
        return this.spy;
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
