package com.mineaurion.aurionchat.common.config;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ConfigurationAdapter {

    void reload();

    String getString(String path, String def);

    // int getInteger(String path, int def);

    boolean getBoolean(String path, boolean def);
//
//    List<String> getStringList(String path, List<String> def);
//
    Map<String, Channel> getChannels();

    default String wrapString(@Nullable UUID player, String str) {
        return str;
    }

    default Optional<String> getChannelByNameOrAlias(String search){
        for(Map.Entry<String, Channel> entry: getChannels().entrySet()){
            String name = entry.getKey();
            Channel channel = entry.getValue();
            if(name.equalsIgnoreCase(search) || channel.alias.equalsIgnoreCase(search)){
                return Optional.of(name);
            }
        }
        return Optional.empty();
    };
}
