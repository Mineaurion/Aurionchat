package com.mineaurion.aurionchat;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class ChatService extends com.mineaurion.aurionchat.common.channel.ChatService {



    public ChatService(String uri, String queueName) throws IOException, TimeoutException {
        super(uri, queueName);
    }

    @Override
    public void sendMessage(String channelName, String message){
        System.out.println(channelName + " " + message);
        // We don't send message if
        if(!channelName.equalsIgnoreCase(AurionChat.config.rabbitmq.serverName.get())){
            List<ServerPlayerEntity> playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
            if(playerList.size() > 0) {
                playerList.forEach( serverPlayerEntity -> {
                    AurionChatPlayer aurionChatPlayer = AurionChat.aurionChatPlayers.get(serverPlayerEntity.getUUID());
                    if(aurionChatPlayer.getChannels().contains(channelName)){
                        serverPlayerEntity.sendMessage(new StringTextComponent(message.replace("&", "§")), Util.NIL_UUID);
                        if(message.contains("@" + serverPlayerEntity.getDisplayName())){
                            serverPlayerEntity.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 10, 1);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void sendAutoMessage(String channelName, String message){
        if(AurionChat.config.options.autoMessage.get()){
            List<ServerPlayerEntity> playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
            if(playerList.size() > 0){
                playerList.forEach(serverPlayerEntity -> {
                    if(FTBRanksAPI.getPermissionValue(serverPlayerEntity, "aurionchat.automessage." + channelName).asBooleanOrFalse()){
                        serverPlayerEntity.sendMessage(new StringTextComponent(message.replace("&", "§")), Util.NIL_UUID);
                    }
                });
            }
        }
    }
}
