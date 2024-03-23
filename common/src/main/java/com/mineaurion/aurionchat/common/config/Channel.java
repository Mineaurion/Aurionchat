package com.mineaurion.aurionchat.common.config;

import com.mineaurion.aurionchat.common.Utils.URL_MODE;

import java.util.Collections;
import java.util.List;

public class Channel {

    public Channel(String format, String alias, List<URL_MODE> urlMode){
        this.format = format;
        this.alias = alias;
        this.urlMode = urlMode;
    }

    public Channel(String format, String alias, URL_MODE urlMode){
        this(
                format,
                alias,
                Collections.singletonList(urlMode)
        );
    }

    public String format;
    public String alias;
    public List<URL_MODE> urlMode;
    public boolean publish;
}
