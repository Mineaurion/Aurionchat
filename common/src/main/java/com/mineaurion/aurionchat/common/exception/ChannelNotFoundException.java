package com.mineaurion.aurionchat.common.exception;

public class ChannelNotFoundException extends Exception{
    public ChannelNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
