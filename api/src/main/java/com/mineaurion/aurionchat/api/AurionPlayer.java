package com.mineaurion.aurionchat.api;

import com.mineaurion.aurionchat.api.model.Named;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Value @NonFinal
public class AurionPlayer implements Named {
    UUID id;
    String name;
    @Nullable String displayName;
}
