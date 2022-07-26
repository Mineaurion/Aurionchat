package com.mineaurion.aurionchat.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

import java.util.Optional;
import java.util.UUID;

public class LuckPermsUtils {

    private final LuckPerms luckPerms;

    public LuckPermsUtils(LuckPerms api){
        this.luckPerms = api;
    }

    private User getUser(UUID uuid){
        return luckPerms.getUserManager().getUser(uuid);
    }

    public Optional<String> getPlayerPrefix(UUID uuid){
        return Optional.ofNullable(this.getUser(uuid).getCachedData().getMetaData().getPrefix());
    }

    public Optional<String> getPlayerSuffix(UUID uuid) {
        return Optional.ofNullable(this.getUser(uuid).getCachedData().getMetaData().getSuffix());
    }

    public boolean hasPermission(UUID uuid, String permission){
        return this.getUser(uuid).getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
