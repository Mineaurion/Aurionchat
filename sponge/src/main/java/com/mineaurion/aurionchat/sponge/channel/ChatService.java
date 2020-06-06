package com.mineaurion.aurionchat.sponge.channel;

import com.mineaurion.aurionchat.common.channel.ChatServiceCommun;
import com.mineaurion.aurionchat.sponge.AurionChat;
import com.mineaurion.aurionchat.sponge.Config;
import com.mineaurion.aurionchat.sponge.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

public class ChatService extends ChatServiceCommun {
    private AurionChat plugin;
    private Config config;
    private Utils utils;

    public ChatService(String uri, AurionChat plugin){
        super(uri);
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.utils = plugin.getUtils();
    }

    @Override
    public void sendMessage(String channel, String message){
        utils.sendMessageToPlayer(channel, message);
        if(config.options.spy){
            plugin.sendConsoleMessage(message);
        }
    }

    @Override
    public void sendAutoMessage(String channel, String message) {
        if(config.options.automessage){
            utils.broadcastToPlayer(channel, message);
        }
    }

    @Override
    public void desactivatePlugin(){
        Sponge.getEventManager().unregisterListeners(plugin);
        Sponge.getCommandManager().getOwnedBy(plugin).forEach(Sponge.getCommandManager()::removeMapping);
        Sponge.getScheduler().getScheduledTasks(plugin).forEach(Task::cancel);
    }
}
