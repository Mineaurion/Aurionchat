package com.mineaurion.aurionchat.fabric;

import com.mineaurion.aurionchat.common.config.ConfigurateConfigAdapter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;

public class FabricConfigAdapter extends ConfigurateConfigAdapter {

    public FabricConfigAdapter(Path path){
        super(path);
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return HoconConfigurationLoader.builder().path(path).build();
    }
}
