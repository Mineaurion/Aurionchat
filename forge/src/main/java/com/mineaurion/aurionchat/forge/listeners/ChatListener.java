package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.forge.AurionChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
public class ChatListener {

    private final AurionChat plugin;

    public ChatListener(AurionChat plugin){
        this.plugin = plugin;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onServerChatEvent(ServerChatEvent event) {

        AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(event.getPlayer().getUUID());
        String currentChannel = aurionChatPlayer.getCurrentChannel();
        Component messageFormat = Utils.processMessage(
                plugin.getConfigurationAdapter().getChannels().get(currentChannel).format,
                GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(event.getMessage())),
                aurionChatPlayer,
                Utils.URL_MODE_ALLOW
        );
        try {
            plugin.getChatService().send(currentChannel, messageFormat);
        } catch (IOException e) {
            LogManager.getLogger().error(e.getMessage());
        }
        event.setCanceled(true); // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}

