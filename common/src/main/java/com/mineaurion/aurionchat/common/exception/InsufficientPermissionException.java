package com.mineaurion.aurionchat.common.exception;

public class InsufficientPermissionException extends Exception{
    public InsufficientPermissionException(String errorMessage){
        super(errorMessage);
    }
}
