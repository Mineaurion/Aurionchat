package com.mineaurion.aurionchat.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mineaurion.aurionchat.api.model.Named;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.*;

public class AurionPacket implements Named, Serializable {
    public static final Gson                           GSON  = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public static       Function<String, AurionPacket> PARSE = AurionPacket::fromJson;

    public static Builder builder() {
        return new Builder();
    }

    public static AurionPacket fromJson(String jsonString) {
        return GSON.fromJson(jsonString, AurionPacket.class);
    }

    public static Builder chat(AurionPlayer player, Object tellRaw) {
        return AurionPacket.builder().type(Type.CHAT).source("ingame").player(player).tellRawData(tellRaw.toString());
    }

    public static Builder autoMessage(Object tellRaw) {
        return AurionPacket.builder().type(Type.AUTO_MESSAGE).source("automessage").displayName("AutoMessage").tellRawData(tellRaw.toString());
    }

    /**
     * Packet type
     */
    @Expose private final           Type         type;
    /**
     * One of: servername, 'discord' or 'ingame' literal
     */
    @Expose private final           String       source;
    /**
     *
     */
    @Expose private final List<String> route;
    /**
     * Related player
     */
    @Expose @Nullable private final AurionPlayer player;
    /**
     * Channel name
     */
    @Expose @Nullable private final String       channel;
    /**
     * Display name of sender (one of: player name, automessage title)
     */
    @Expose @Nullable private final String       displayName;
    /**
     * Only the string of the message without any colors or stuff from the game
     */
    @Expose @NotNull private final  String       displayString;
    /**
     * What ingame players see
     */
    @Expose @NotNull private final  String       tellRawData;

    public AurionPacket(
            Type type, String source, List<String> route, @Nullable AurionPlayer player, @Nullable String channel, @Nullable String displayName,
            @NotNull String tellRawData
    ) {
        this.type          = type;
        this.source        = source;
        this.route = route;
        this.player        = player;
        this.channel       = channel;
        this.displayName   = displayName;
        this.tellRawData   = tellRawData;
        this.displayString = PlainTextComponentSerializer.plainText().serialize(getComponent());
    }

    /**
     * What to display in plaintext environments
     */
    public String getRawDisplay() {
        return displayName + ' ' + type.name().toLowerCase() + displayString;
    }

    public Component getComponent() {
        return gson().deserialize(tellRawData);
    }

    @Override
    public @Nullable String getName() {
        return Optional.ofNullable(player).map(Named::getBestName).orElse(null);
    }

    @Override
    public @Nullable String getDisplayName() {
        return displayName;
    }

    public Type getType() {
        return this.type;
    }

    public String getSource() {
        return this.source;
    }

    public List<String> getRoute() {
        return this.route;
    }

    public @Nullable AurionPlayer getPlayer() {
        return this.player;
    }

    public @Nullable String getChannel() {
        return this.channel;
    }

    public @NotNull String getDisplayString() {
        return this.displayString;
    }

    public @NotNull String getTellRawData() {
        return this.tellRawData;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public Builder toBuilder() {
        return new Builder().type(this.type)
                .source(this.source)
                .player(this.player)
                .channel(this.channel)
                .displayName(this.displayName)
                .tellRawData(this.tellRawData);
    }

    public enum Type {
        @SerializedName("chat") CHAT,
        @SerializedName("automessage") AUTO_MESSAGE,
        @SerializedName("event_join") EVENT_JOIN,
        @SerializedName("event_achievement") EVENT_ACHIEVEMENT,
    }

    public static class Builder {
        private           Type   type;
        private           String source;
        private @Nullable AurionPlayer player;
        private @Nullable String channel;
        private @Nullable String displayName;
        private @NotNull  String tellRawData = "";

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder player(@Nullable AurionPlayer player) {
            this.player = player;
            return this;
        }

        public Builder channel(@Nullable String channel) {
            this.channel = channel;
            return this;
        }

        public Builder displayName(@Nullable String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder tellRawData(@NotNull String tellRawData) {
            this.tellRawData = tellRawData;
            return this;
        }

        public AurionPacket build() {
            return new AurionPacket(this.type, this.source, new ArrayList<>(), this.player, this.channel, this.displayName, this.tellRawData);
        }
    }
}
