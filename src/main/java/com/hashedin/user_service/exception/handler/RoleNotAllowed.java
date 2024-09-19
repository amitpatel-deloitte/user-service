package com.hashedin.user_service.exception.handler;

public class RoleNotAllowed extends RuntimeException{
    public RoleNotAllowed(String message){
        super(message);
    }
}
