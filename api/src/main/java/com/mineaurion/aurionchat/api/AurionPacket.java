package com.mineaurion.aurionchat.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mineaurion.aurionchat.api.model.Named;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Optional;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor // make public for gson
public class AurionPacket implements Named, Serializable {
    public static Gson gson = new Gson();

    public static AurionPacket parse(String json) {
        return gson.fromJson(json, AurionPacket.class);
    }

    public static Builder chat(AurionPlayer player, String message, Object tellRaw) {
        return AurionPacket.builder()
                .type(Type.CHAT)
                .source("ingame")
                .player(player)
                .detail(message)
                .tellRawData(tellRaw.toString());
    }

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

    /** display name of sender (one of: player name, automessage title) */
    @Default @Nullable String displayName = null;

    /** detail data (one of: message text, join text, achievement text) */
    @Default @Nullable String detail = null;

    /** what ingame players see */
    @NotNull String tellRawData;

    /** what to display in plaintext environments */
    @SuppressWarnings("ConstantValue") // false positive bcs of lombok
    public String getRawDisplay() {
        return displayName + ' ' + type.verb + (detail==null?"": ": " + detail);
    }

    public Component getComponent() {
        return gson().deserialize(tellRawData);
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
        @SerializedName("automessage") AUTO_MESSAGE("broadcasted");

        String verb;
    }
}
