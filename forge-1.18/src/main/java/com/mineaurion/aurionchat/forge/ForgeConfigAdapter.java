package com.mineaurion.aurionchat.forge;

import com.mineaurion.aurionchat.common.config.ConfigurateConfigAdapter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;

public class ForgeConfigAdapter extends ConfigurateConfigAdapter {

    public ForgeConfigAdapter(AurionChat plugin, Path path){
        super(plugin, path);
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return HoconConfigurationLoader.builder().path(path).build();
    }
}
