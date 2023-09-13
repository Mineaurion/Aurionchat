package com.mineaurion.aurionchat.sponge;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;


@ConfigSerializable
public class Config{
    public final static TypeToken<Config> type = TypeToken.of(Config.class);

    @Setting public Rabbitmq rabbitmq = new Rabbitmq();

    @ConfigSerializable
    public static class Rabbitmq{
        @Setting
        public String uri = "amqp://guest:guest@localhost:5672/";
    }

    @Setting public Options options = new Options();
    @ConfigSerializable
    public static class Options{
        @Setting
        @Comment("Allow console to print all message")
        public boolean spy = true;
        @Setting
        public boolean automessage = true;
    }

    @Setting()
    @Comment("Channel name must be alphanumeric, no special char")
    public Map<String, Channel> channels = ImmutableMap.of("global", new Channel());
    @ConfigSerializable
    public static class Channel{
        @Setting
        public String format = "[GLOBAL] {prefix}{display_name} : &f{message}";
        @Setting()
        @Comment("Define an alias of the channel for command purpose")
        public String alias = "g";
    }

}
