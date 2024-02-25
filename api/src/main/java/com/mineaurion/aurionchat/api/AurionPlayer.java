package com.mineaurion.aurionchat.api;

import com.mineaurion.aurionchat.api.model.Player;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

@Value @NonFinal
public class AurionPlayer implements Player {
    UUID id;
    String name;
    @Nullable String prefix;
    @Nullable String suffix;
    @Nullable String displayName;

    public AurionPlayer(UUID id, String name, @Nullable String prefix, @Nullable String suffix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.displayName = Optional.ofNullable(prefix).orElse("") + name + Optional.ofNullable(suffix).orElse("");
    }
}
