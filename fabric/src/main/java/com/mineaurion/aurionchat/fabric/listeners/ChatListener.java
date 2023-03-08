package com.mineaurion.aurionchat.fabric.listeners;

import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.fabric.AurionChat;
import com.mineaurion.aurionchat.fabric.AurionChatPlayer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public class ChatListener implements ServerMessageEvents.AllowChatMessage {

    private final AurionChat plugin;

    public ChatListener(AurionChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean allowChatMessage(SignedMessage message, ServerPlayerEntity sender, Parameters params) {
        AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(sender.getUuid());
        String currentChannel = aurionChatPlayer.getCurrentChannel();
        String messageFormat = Utils.processMessage(AurionChat.config.channels.get(currentChannel).format,  message.signedBody().content().plain(), aurionChatPlayer);
        try {
            ChatService.getInstance().send(currentChannel, messageFormat);
        } catch (IOException e){
            this.plugin.getlogger().severe(e.getMessage());
        }
        // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
        // After we can use CHAT_MESSAGE instead of this event
        return false;
    }
}
