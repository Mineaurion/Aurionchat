package com.mineaurion.aurionchat.bukkit;

import com.mineaurion.aurionchat.common.plugin.AurionChatBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class Bootstrap extends JavaPlugin implements AurionChatBootstrap {
    private final AurionChat plugin;


    public Bootstrap(){
        this.plugin = new AurionChat(this);
    }

    @Override
    public void onEnable(){
        plugin.enable();
    }

    @Override
    public void onDisable() {
        plugin.disable();
    }

    @Override
    public Path getConfigDirectory() {
        return getDataFolder().toPath().toAbsolutePath();
    }
}
