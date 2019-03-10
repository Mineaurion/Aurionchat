package com.mineaurion.aurionchat.common;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.UserData;

import java.util.Optional;
import java.util.UUID;

public class LuckPermsUtils {

    private LuckPermsApi api;

    public LuckPermsUtils(LuckPermsApi api){
        this.api = api;
    }

    private Optional<MetaData> getPlayerMetaData(Object player, UUID uuid){
        Optional<User> user = api.getUserSafe(uuid);
        Optional<MetaData> metaData = Optional.empty();
        if(user.isPresent()){
            Contexts contexts = api.getContextsForPlayer(player);
            UserData userData = user.get().getCachedData();
            metaData = Optional.of(userData.getMetaData(contexts));
        }
        return metaData;
    }

    public Optional<String> getPlayerPrefix(Object player, UUID uuid){
        Optional<MetaData> metaData = getPlayerMetaData(player, uuid);
        Optional<String> prefix = Optional.empty();
        if(metaData.isPresent()){
            prefix = Optional.ofNullable(metaData.get().getPrefix());
        }
        return prefix;
    }

    public Optional<String> getPlayerSuffix(Object player, UUID uuid){
        Optional<MetaData> metaData = getPlayerMetaData(player, uuid);
        Optional<String> suffix = Optional.empty();
        if(metaData.isPresent()){
            suffix = Optional.ofNullable(metaData.get().getSuffix());
        }
        return suffix;
    }
}
