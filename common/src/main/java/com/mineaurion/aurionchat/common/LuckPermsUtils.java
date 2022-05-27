package com.mineaurion.aurionchat.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

import java.util.UUID;

public class LuckPermsUtils {

    private final LuckPerms luckPerms;

    public LuckPermsUtils(LuckPerms api){
        this.luckPerms = api;
    }

    private User getUser(UUID uuid){
        return luckPerms.getUserManager().getUser(uuid);
    }

    public String getPlayerPrefix(UUID uuid){
        return this.getUser(uuid).getCachedData().getMetaData().getPrefix();
    }

    public String getPlayerSuffix(UUID uuid) {
        return this.getUser(uuid).getCachedData().getMetaData().getSuffix();
    }
}
