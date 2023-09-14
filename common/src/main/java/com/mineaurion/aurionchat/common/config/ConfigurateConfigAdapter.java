package com.mineaurion.aurionchat.common.config;

import com.google.common.base.Splitter;
import com.mineaurion.aurionchat.common.AbstractAurionChat;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ConfigurateConfigAdapter implements ConfigurationAdapter {
    private final Path path;
    private ConfigurationNode root;

    public ConfigurateConfigAdapter(Path path){
        this.path = path;
        reload();
    }

    protected abstract ConfigurationLoader<? extends ConfigurationNode> createLoader(Path path);

    @Override
    public void reload() {
        ConfigurationLoader<? extends ConfigurationNode> loader = createLoader(this.path);
        try {
            this.root = loader.load();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private ConfigurationNode resolvePath(String path){
        if(this.root == null){
            throw new RuntimeException("Config is not loaded");
        }
        return this.root.node(Splitter.on('.').splitToList(path).toArray());
    }

    @Override
    public String getString(String path, String def) {
        return resolvePath(path).getString(def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return resolvePath(path).getBoolean(def);
    }

    @Override
    public Map<String, Channel> getChannels() {
        ConfigurationNode node = resolvePath("channels");
        if(node.virtual()){
            return new HashMap<>();
        }

        return node.childrenMap().entrySet().stream().collect(
                Collectors.toMap(
                        k -> k.getKey().toString(),
                        v -> new Channel(v.getValue().node("format").getString(), v.getValue().node("alias").getString())
                )
        );
    }

    @Override
    public Optional<String> getChannelByNameOrAlias(String search) {
        for(Map.Entry<String, Channel> entry: getChannels().entrySet()){
            String name = entry.getKey();
            Channel channel = entry.getValue();
            if(name.equalsIgnoreCase(search) || channel.alias.equalsIgnoreCase(search)){
                return Optional.of(name);
            }
        }
        return Optional.empty();
    }
}
