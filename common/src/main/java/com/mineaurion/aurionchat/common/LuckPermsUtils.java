package com.mineaurion.aurionchat.common;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;

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

    public Optional<String> getPlayerPreffix(UUID uuid){
        return Optional.ofNullable(getUser(uuid).getCachedData().getMetaData().getPrefix());
    }

    public Optional<String> getPlayerSuffix(UUID uuid) {
        return Optional.ofNullable(getUser(uuid).getCachedData().getMetaData().getSuffix());
    }

    public boolean hasPermission(UUID uuid, String permission, boolean explicitly){
        Tristate result = getUser(uuid)
                .getCachedData()
                .getPermissionData()
                .queryPermission(permission)
                .result();
        return explicitly ? result == Tristate.TRUE : result != Tristate.FALSE;
    }
}
