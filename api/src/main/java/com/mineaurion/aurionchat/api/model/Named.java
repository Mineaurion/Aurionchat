package com.mineaurion.aurionchat.api.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Named {
    @Nullable String getName();

    @Nullable String getDisplayName();

    default String getBestName() {
        return getBestName(toString());
    }

    @Contract("null -> _; !null -> !null")
    default String getBestName(String fallback) {
        return Optional.ofNullable(getDisplayName())
                .orElseGet(() -> Optional.ofNullable(getName())
                        .orElse(fallback));
    }
}
