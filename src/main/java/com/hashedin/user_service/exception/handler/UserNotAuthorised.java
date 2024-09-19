package com.hashedin.user_service.exception.handler;

public class UserNotAuthorised extends RuntimeException {
    public UserNotAuthorised (String message){
        super(message);
    }
}
