package com.mineaurion.aurionchat.api;

import com.google.gson.annotations.Expose;
import com.mineaurion.aurionchat.api.model.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class AurionPlayer implements Player {
    @Expose           UUID   id;
    @Expose           String name;
    @Expose @Nullable String prefix;
    @Expose @Nullable String suffix;
    @Expose @Nullable String displayName;

    public AurionPlayer(UUID id, String name, @Nullable String prefix, @Nullable String suffix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.displayName = Optional.ofNullable(prefix).orElse("") + name + Optional.ofNullable(suffix).orElse("");
    }

    @Override
    public @Nullable String getName() {
        return name;
    }

    @Override
    public @Nullable String getDisplayName() {
        return displayName;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public @Nullable String getPrefix() {
        return prefix;
    }

    @Override
    public @Nullable String getSuffix() {
        return suffix;
    }
}
