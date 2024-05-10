package com.mineaurion.aurionchat.common.commands;

import com.mineaurion.aurionchat.api.AurionPacket;
import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.Utils;
import com.mineaurion.aurionchat.common.plugin.AbstractAurionChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.io.IOException;
import java.util.Collections;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_RED;

public class ChatCommandCommon {
    private final AbstractAurionChat plugin;

    public ChatCommandCommon(AbstractAurionChat plugin){
        this.plugin = plugin;
    }

    public AbstractAurionChat getPlugin(){
        return plugin;
    }



    public boolean onCommand(AurionChatPlayer aurionChatPlayers, Component message, String channel, String format) {
        aurionChatPlayers.addChannel(channel);
        Component component = Utils.processMessage(format, message, aurionChatPlayers, Collections.singletonList(Utils.URL_MODE.ALLOW));
        AurionPacket.Builder packet = AurionPacket.chat(aurionChatPlayers, GsonComponentSerializer.gson().serialize(component))
                .channel(channel);
        try {
            plugin.getChatService().send(packet);
            return true;
        } catch (IOException e) {
            aurionChatPlayers.sendMessage(text("The server returned an error, your message could not be sent").color(DARK_RED));
            System.err.println(e.getMessage());
        }
        return false;
    }
}
