package com.hashedin.user_service.model;

import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {

    private String token;
    private long expiresIn;

}
