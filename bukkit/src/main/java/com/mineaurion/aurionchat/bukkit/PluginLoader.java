package com.mineaurion.aurionchat.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginLoader extends JavaPlugin {

    private final AurionChat plugin;

    public PluginLoader(){
        plugin = new AurionChat(this);
    }

    @Override
    public void onEnable(){
        plugin.onEnable();
    }

    @Override
    public void onDisable(){
        plugin.onDisable();
    }
}
