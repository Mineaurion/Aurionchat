package com.mineaurion.aurionchat.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;

import java.util.Optional;
import java.util.UUID;

public class LuckPermsUtils {

    private LuckPerms luckPerms;

    public LuckPermsUtils(LuckPerms api){
        this.luckPerms = api;
    }


    private CachedMetaData getMetaData(UUID uuid){
        User user = luckPerms.getUserManager().getUser(uuid);
        CachedMetaData metaData = null;
        if(user != null){
            Optional<QueryOptions> queryOptions = luckPerms.getContextManager().getQueryOptions(user);
            if(queryOptions.isPresent()) {
                CachedPermissionData permissionData = user.getCachedData().getPermissionData(queryOptions.get());
                metaData = user.getCachedData().getMetaData(queryOptions.get());
            }
        }
        return metaData;
    }

    public String getPlayerPrefix(UUID uuid){
        Optional<CachedMetaData> cachedMetaData = Optional.ofNullable(getMetaData(uuid));
        return cachedMetaData.map(CachedMetaData::getPrefix).orElse("");
    }

    public String getPlayerSuffix(UUID uuid) {
        Optional<CachedMetaData> cachedMetaData =  Optional.ofNullable(getMetaData(uuid));
        return cachedMetaData.map(CachedMetaData::getSuffix).orElse("");
    }
}
