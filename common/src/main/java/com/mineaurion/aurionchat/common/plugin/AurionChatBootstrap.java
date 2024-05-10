package com.mineaurion.aurionchat.common.plugin;

import java.io.InputStream;
import java.nio.file.Path;

public interface AurionChatBootstrap {
    /**
     * Gets the plugins main data storage directory
     *
     * <p>Bukkit: ./plugins/Aurionchat</p>
     * <p>Sponge: ./Aurionchat/</p>
     * <p>Fabric: ./mods/Aurionchat</p>
     * <p>Forge: ./config/Aurionchat</p>
     *
     * @return the platforms data folder
     */
    Path getConfigDirectory();

    default InputStream getResourceStream(String path){
        return getClass().getClassLoader().getResourceAsStream(path);
    }
}
