package com.mineaurion.aurionchat.sponge;

import com.mineaurion.aurionchat.common.config.ConfigurateConfigAdapter;
import com.mineaurion.aurionchat.common.config.ConfigurationAdapter;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.nio.file.Path;

public class SpongeConfigAdapter extends ConfigurateConfigAdapter implements ConfigurationAdapter {

    public SpongeConfigAdapter(AurionChat plugin, Path path){
        super(plugin, path);
    }

    @Override
    protected ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path) {
        return HoconConfigurationLoader.builder().path(path).build();
    }
}
