package com.mineaurion.aurionchat.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class AurionPacket implements Serializable {
    public static Gson gson = new Gson();

    public static AurionPacket parse(String json) {
        return gson.fromJson(json, AurionPacket.class);
    }

    public static Builder autoMessage(String channel, String message, Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.AUTO_MESSAGE)
                .source("automessage")
                .channelName(channel)
                .displayName("AutoMessage")
                .detail(message)
                .tellRawData(tellRaw.toString());
    }

    /** packet type */
    @Default Type type = Type.CHAT;

    /** id of related player */
    @Default @Nullable UUID playerId = null;

    /** one of: servername, 'discord' literal */
    String source;

    /** channel name */
    String channelName;

    /** display name of sender (one of: player name, automessage title) */
    String displayName;

    /** detail data (one of: message text, join text, achievement text) */
    @Default @Nullable String detail = null;

    /** what ingame players see */
    @NotNull String tellRawData;

    /** what to display in plaintext environments */
    public String getRawDisplay() {
        return displayName + ' ' + type.verb + (detail==null?"": ": " + detail);
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    @Getter
    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public enum Type {
        @SerializedName("chat") CHAT("wrote"),
        @SerializedName("automessage") AUTO_MESSAGE("broadcasted");

        String verb;
    }
}
