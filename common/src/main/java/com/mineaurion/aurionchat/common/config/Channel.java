package com.mineaurion.aurionchat.common.config;

public class Channel {

    public Channel(String format, String alias, int urlMode){
        this.format = format;
        this.alias = alias;
        this.urlMode = urlMode;
    }

    public String format;
    public String alias;
    public int urlMode;
}
