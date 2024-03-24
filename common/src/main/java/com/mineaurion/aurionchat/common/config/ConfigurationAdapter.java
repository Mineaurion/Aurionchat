package com.mineaurion.aurionchat.common.config;

import com.mineaurion.aurionchat.common.AbstractAurionChat;
import com.mineaurion.aurionchat.common.Utils;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

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

    default List<Utils.URL_MODE> stringListToUrlMode(List<String> slist) {
        List<Utils.URL_MODE> list = new ArrayList<>();
        for (String val : slist) {
            try {
                list.add(Utils.URL_MODE.valueOf(val));
            } catch (IllegalArgumentException e){
                throw new RuntimeException("This url mode '" + val + "' is not supported. We supported this mode : " + Arrays.toString(Utils.URL_MODE.values()));
            }
        }
        return list;
    }
}
