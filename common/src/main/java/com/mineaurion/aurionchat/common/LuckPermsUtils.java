package com.mineaurion.aurionchat.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.Optional;
import java.util.UUID;

public class LuckPermsUtils {

    private LuckPerms api;

    public LuckPermsUtils(LuckPerms api){
        this.api = api;
    }


    private Optional<CachedMetaData> getMetaData(UUID uuid){
        Optional<CachedMetaData> cachedMetaData = Optional.empty();
        if(this.api != null){
            User user = this.api.getUserManager().getUser(uuid);
            if(user != null){
                Optional<QueryOptions> context = api.getContextManager().getQueryOptions(user);
                if(context.isPresent()){
                    cachedMetaData = Optional.of(user.getCachedData().getMetaData(context.get()));
                }
            }
        }
        return cachedMetaData;
    }

    public Optional<String> getPlayerPrefix(UUID uuid){
        Optional<CachedMetaData> cachedMetaData = getMetaData(uuid);
        return Optional.ofNullable(cachedMetaData.get().getPrefix());
    }

    public Optional<String> getPlayerSuffix(UUID uuid) {
        Optional<CachedMetaData> cachedMetaData = getMetaData(uuid);
        return Optional.ofNullable(cachedMetaData.get().getSuffix());
    }
}
