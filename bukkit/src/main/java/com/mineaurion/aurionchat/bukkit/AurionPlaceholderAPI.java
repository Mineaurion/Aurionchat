package com.mineaurion.aurionchat.bukkit;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class AurionPlaceholderAPI extends PlaceholderExpansion {
    private final AurionChat plugin;

    public AurionPlaceholderAPI(AurionChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "aurionchat";
    }

    @Override
    public String getAuthor() {
        return "Yann151924";
    }

    @Override
    public String getVersion() {
        return "0.14.4";
    }
}
