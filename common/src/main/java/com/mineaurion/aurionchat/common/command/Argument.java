package com.mineaurion.aurionchat.common.command;

import com.mineaurion.aurionchat.common.locale.Message;
import net.kyori.adventure.text.Component;

public class Argument {

    private final String name;
    private final boolean required;

    public Argument(String name, boolean required){
        this.name = name;
        this.required = required;
    }

    public String getName(){
        return this.name;
    }

    public Component asComponent(){
        return (this.required ? Message.REQUIRED_ARGUMENT : Message.OPTIONAL_ARGUMENT).build(this.name);
    }
}