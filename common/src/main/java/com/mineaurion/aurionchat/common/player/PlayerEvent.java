package com.mineaurion.aurionchat.common.player;

import com.mineaurion.aurionchat.common.AurionChatPlayer;

public final class PlayerEvent {
    public final AurionChatPlayer player;
    public final Type type;

    public PlayerEvent(AurionChatPlayer player, Type type) {
        this.player = player;
        this.type = type;
    }

    public enum Type {
        Join("join", "joined"),
        Leave("leave", "left");

        Type(String name, String verb) {
            this.name = name;
            this.verb = verb;
        }

        public final String name;
        public final String verb;
    }
}
