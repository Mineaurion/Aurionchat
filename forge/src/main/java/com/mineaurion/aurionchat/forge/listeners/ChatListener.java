package com.mineaurion.aurionchat.forge.listeners;

import com.mineaurion.aurionchat.api.AurionPacket;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.config.Channel;
import com.mineaurion.aurionchat.forge.AurionChat;
import net.kyori.adventure.text.Component;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

public class ChatListener {

    private final AurionChat plugin;

    public ChatListener(AurionChat plugin) {
        this.plugin = plugin;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onServerChatEvent(ServerChatEvent event) {

        AurionChatPlayer aurionChatPlayer = this.plugin.getAurionChatPlayers().get(event.getPlayer().getUUID());
        String currentChannel = aurionChatPlayer.getCurrentChannel();
        Channel channel = plugin.getConfigurationAdapter().getChannels().get(currentChannel);
        Component component = Utils.processMessage(
                channel.format,
                gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(event.getMessage())),
                aurionChatPlayer,
                channel.urlMode
        );
        AurionPacket.Builder packet = AurionPacket.chat(
                        aurionChatPlayer,
                        event.getRawText(),
                        gson().serialize(component))
                .channel(currentChannel);
        try {
            plugin.getChatService().send(packet);
        } catch (IOException e) {
            LogManager.getLogger().error(e.getMessage());
        }
        event.setCanceled(true); // TODO: need to remove that. Need to adapt rabbitmq to a fanout exchange for the chat.
    }
}
