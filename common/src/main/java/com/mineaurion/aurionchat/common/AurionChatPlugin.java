package com.mineaurion.aurionchat.common;

import com.mineaurion.aurionchat.common.logger.PluginLogger;

public interface AurionChatPlugin {

    /**
     * Gets a wrapped logger instance for the platform.
     *
     * @return the plugin's logger
     */
    PluginLogger getlogger();

    String getServerName();
}
