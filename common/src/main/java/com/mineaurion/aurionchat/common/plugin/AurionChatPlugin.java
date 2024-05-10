package com.mineaurion.aurionchat.common.plugin;

import com.mineaurion.aurionchat.common.AurionChatPlayer;
import com.mineaurion.aurionchat.common.ChatService;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import com.mineaurion.aurionchat.common.logger.PluginLogger;

import java.util.Map;
import java.util.UUID;

public interface AurionChatPlugin {

    String MOD_ID = "aurionchat";

    String NAME = "AurionChat";

    AurionChatBootstrap getBootstrap();

    PluginLogger getLogger();

    ChatService getChatService();

    ConfigurationAdapter getConfigurationAdapter();

    Map<UUID, AurionChatPlayer> getAurionChatPlayers();
}
