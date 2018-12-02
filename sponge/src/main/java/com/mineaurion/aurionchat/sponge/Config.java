package com.mineaurion.aurionchat.sponge;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;

import java.util.Map;

@ConfigSerializable
public class Config {
    public final static TypeToken<Config> type = TypeToken.of(Config.class);

    @Setting public Rabbitmq rabbitmq = new Rabbitmq();
    @ConfigSerializable
    public static class Rabbitmq{
        @Setting
        public String uri = "amqp://guest:guest@localhost:5672/";
        @Setting(comment = "Must Be unique beetwen all of your servers")
        public String servername = "ServerName";
    }

    @Setting public Options options = new Options();
    @ConfigSerializable
    public static class Options{
        @Setting
        public SoundType sound = SoundTypes.ENTITY_EXPERIENCE_ORB_PICKUP;
        @Setting(comment = "Allow console to print all message")
        public boolean spyConsole = true;
        @Setting
        public boolean automessage = true;
    }

    @Setting(comment = "Channel name must be alphanumeric, no special char")
    public Map<String, Channel> channels = ImmutableMap.of("global", new Channel());
    @ConfigSerializable
    public static class Channel{
        @Setting
        public String format = "[GLOBAL] {prefix}{display_name} : &f{message}";
        @Setting(comment = "Define an alias of the channel for command purpose")
        public String alias = "g";
    }

    @Setting
    public Map<String, Automessage> automessage = ImmutableMap.of("auto-fr", new Automessage());
    @ConfigSerializable
    public static class Automessage{
        @Setting(comment = "Define the permission for the channel of automessage")
        public String permission = "";
    }

}
