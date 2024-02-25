package com.mineaurion.aurionchat.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mineaurion.aurionchat.api.model.Named;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Optional;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor // make public for gson
public class AurionPacket implements Named, Serializable {
    public static Gson gson = new Gson();

    public static AurionPacket parse(String json) {
        return gson.fromJson(json, AurionPacket.class);
    }

    /**
     * create a packet builder for a chat message
     * @param player the related player
     * @param message the raw, sanitized text content of the message
     * @param tellRaw shown to ingame players
     * @return builder; should be extended with {@link Builder#channel(String)}
     */
    public static Builder chat(AurionPlayer player, String message, Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.CHAT)
                .source("ingame")
                .player(player)
                .detail(message)
                .tellRawData(tellRaw.toString());
    }

    /**
     * create a packet builder for a player event
     * @param player the related player
     * @param type the exact type of player event
     * @param tellRaw shown to ingame players
     * @return builder; should be extended with {@link Builder#detail(String)} and {@link Builder#channel(String)}
     */
    public static Builder event(AurionPlayer player, Type type, Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.CHAT)
                .source("ingame")
                .player(player)
                .tellRawData(tellRaw.toString());
    }

    /**
     * create a packet builder for an automessage
     * @param message the raw, sanitized message
     * @param tellRaw shown to ingame players
     * @return builder; should be extended with {@link Builder#channel(String)}
     */
    public static Builder autoMessage(String message, Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.AUTO_MESSAGE)
                .source("automessage")
                .displayName("AutoMessage")
                .detail(message)
                .tellRawData(tellRaw.toString());
    }

    /** packet type */
    Type type;

    /** one of: servername, 'discord' or 'ingame' literal */
    String source;

    /** related player */
    @Default @Nullable AurionPlayer player = null;

    /** channel name */
    @Default @Nullable String channel = null;

    /** sanitized display name of sender (one of: player name, automessage title) */
    @Default @Nullable String displayName = null;

    /** sanitized detail data (one of: message text, join text, achievement text) */
    @Default @Nullable String detail = null;

    /** what ingame players see */
    @NotNull String tellRawData;

    /** what to display in plaintext environments */
    @SuppressWarnings("ConstantValue") // false positive bcs of lombok
    public String getRawDisplay() {
        return displayName + ' ' + type.verb + (detail==null?"": ": " + detail);
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    @Override
    @SuppressWarnings({"ConstantValue", "OptionalOfNullableMisuse"}) // false positive bcs of lombok
    public @Nullable String getName() {
        return Optional.ofNullable(player)
                .map(Named::getBestName)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("ConstantValue") // false positive bcs of lombok
    public @Nullable String getDisplayName() {
        return displayName;
    }

    @Getter
    @RequiredArgsConstructor
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public enum Type {
        @SerializedName("chat") CHAT("wrote"),
        @SerializedName("join") JOIN("joined"),
        @SerializedName("leave") LEAVE("left"),
        @SerializedName("death") DEATH("died"),
        @SerializedName("advancement") ADVANCEMENT("made advancement"),
        @SerializedName("automessage") AUTO_MESSAGE("broadcasted");

        String verb;
    }
}
