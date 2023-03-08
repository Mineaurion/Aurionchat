package com.mineaurion.aurionchat.fabric;

import com.google.common.collect.ImmutableMap;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.Map;

@ConfigEntries(includeAll = true)
public class Config extends me.lortseam.completeconfig.data.Config {

    public Config(){
        super(AurionChat.ID);
    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static final class Rabbitmq implements ConfigGroup {

        @ConfigEntry(requiresRestart = true)
        public static String uri = "amqp://guest:guest@localhost:5672/";
        @ConfigEntry(requiresRestart = true, comment = "Must Be unique beetwen all of your servers")
        public static String servername = "ServerName";

    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static class Options implements ConfigGroup{

        @ConfigEntry(comment = "Allow console to print all message")
        public static boolean spy = true;
        public static boolean automessage = true;
    }

    @ConfigEntry(comment = "Channel name must be alphanumeric, no special char")
    public Map<String, Channel> channels = ImmutableMap.of("global", new Channel());

    @ConfigSerializable
    public static class Channel implements ConfigGroup{
        @ConfigEntry
        public String format = "[GLOBAL] {prefix}{display_name} : &f{message}";

        @ConfigEntry(comment = "Define an alias of the channel for command purpose")
        public String alias = "g";
    }

}
