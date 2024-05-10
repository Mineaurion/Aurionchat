package com.mineaurion.aurionchat.common.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public enum CommandSpec {

    JOIN("/%s join <channel>",
            arg("channel", true)),

    LEAVE("/%s leave <channel>",
            arg("channel", true)),

    SPY("/%s spy <channel>",
            arg("channel", true)),

    ALL_LISTEN("/%s alllisten"),

    RELOAD("/%s reload")
    ;



    public final static String PERMISSION_BY_CHANNEL = "aurionchat.channel.%s.%s";
    private final String usage;
    private final List<Argument> args;

    CommandSpec(String usage, PartialArgument... args){
        this.usage = usage;
        this.args = args.length == 0 ? null : Arrays.stream(args)
                .map(builder -> {
                    String key = builder.id.replace(".", "").replace(' ', '-');
                    return new Argument(builder.name, builder.required);
                })
                .collect(Collectors.toList());
    }

    public String usage(){
        return this.usage;
    }

    public List<Argument> args(){
        return this.args;
    }

    private static PartialArgument arg(String name, boolean required){
        return new PartialArgument(name, name, required);
    }

    private static final class PartialArgument {
        private final String id;
        private final String name;
        private final boolean required;

        private PartialArgument(String id, String name, boolean required){
            this.id = id;
            this.name = name;
            this.required = required;
        }
    }
};