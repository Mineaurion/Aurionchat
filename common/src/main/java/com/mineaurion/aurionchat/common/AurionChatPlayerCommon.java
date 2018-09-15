package com.mineaurion.aurionchat.common;

import java.util.Set;
import java.util.UUID;

public abstract class AurionChatPlayerCommon {

    protected UUID uuid;
    protected String name;
    protected String nickname;
    protected String currentChannel;
    protected Set<String> listening;
    protected boolean online;
   // private Player player;
   protected boolean spy;


    public AurionChatPlayerCommon(UUID uuid, String name, String currentChannel, Set<String> listening, String nickname, boolean spy){
        this.uuid = uuid;
        this.name = name;
        this.nickname = nickname;
        this.currentChannel = currentChannel;
        this.listening = listening;
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
            addListening(channel);
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

    abstract public void setOnline(boolean online);

//    public void setOnline(boolean online){
//        this.online = online;
//        if(this.online) {
//            this.player = Bukkit.getPlayer(this.uuid);
//        }
//        else{
//            this.player = null;
//        }
//    }


//    public Player getPlayer(){
//        return this.online ? this.player : null;
//    }

    public boolean isSpy(){
        return this.spy;
    }

//    public static AurionChatPlayer getAurionChatPlayer(Player player){
//        for(AurionChatPlayer aurionChatPlayer: AurionChat.players){
//            if(aurionChatPlayer.getUuid().toString().equals(player.getUniqueId().toString())){
//                return aurionChatPlayer;
//            }
//        }
//        return null;
//    }
//
//    public static AurionChatPlayer getAurionChatPlayer(UUID uuid){
//        for( AurionChatPlayer aurionChatPlayer: AurionChat.players){
//            if(aurionChatPlayer.getUuid().toString().equals(uuid.toString())){
//                return aurionChatPlayer;
//            }
//        }
//        return null;
//    }
//
//    public static AurionChatPlayer getAurionChatPlayer(String name){
//        for( AurionChatPlayer aurionChatPlayer: AurionChat.players){
//            if(aurionChatPlayer.getName().equalsIgnoreCase(name)){
//                return aurionChatPlayer;
//            }
//        }
//        return null;
//    }
//
//    public static AurionChatPlayer getOnlineAurionChatPlayer(Player player){
//        for (AurionChatPlayer aurionChatPlayer: AurionChat.onlinePlayers){
//            if(aurionChatPlayer.getUuid().toString().equals(player.getUniqueId().toString())){
//                return aurionChatPlayer;
//            }
//        }
//        return null;
//    }
//
//    public static AurionChatPlayer getOnlineAurionChatPlayer(UUID uuid){
//        for (AurionChatPlayer aurionChatPlayer: AurionChat.onlinePlayers){
//            if(aurionChatPlayer.getUuid().toString().equals(uuid.toString())){
//                return aurionChatPlayer;
//            }
//        }
//        return null;
//    }
//
//    public static AurionChatPlayer getOnlineAurionChatPlayer(String name){
//        for (AurionChatPlayer aurionChatPlayer: AurionChat.onlinePlayers){
//            if(aurionChatPlayer.getName().equalsIgnoreCase(name)){
//                return aurionChatPlayer;
//            }
//        }
//        return null;
//    }

}
