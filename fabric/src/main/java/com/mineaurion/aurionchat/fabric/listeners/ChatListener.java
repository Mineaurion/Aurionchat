package com.mineaurion.aurionchat.fabric.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.fabric.AurionChat;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.message.MessageType.Parameters;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

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
        Channel channel = plugin.getConfigurationAdapter().getChannels().get(currentChannel);
        Component messageFormat = Utils.processMessage(
                channel.format,
                GsonComponentSerializer.gson().deserialize(Text.Serializer.toJson(message.getContent())),
                aurionChatPlayer,
                channel.urlMode
        );
        try {
            ChatService chatService = plugin.getChatService();
            if (channel.publish)
                chatService.send(currentChannel, messageFormat);
            else chatService.deliver(currentChannel, messageFormat);
        } catch (IOException e){
            this.plugin.getlogger().severe(e.getMessage());
        }
        // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
        // After we can use CHAT_MESSAGE instead of this event
        return false;
    }
}
