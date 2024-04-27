package com.mineaurion.aurionchat.api.model;

import java.util.UUID;

public interface Player extends Named {
    String getDisplayName();

    UUID getId();

    String getPrefix();

    String getSuffix();
}
