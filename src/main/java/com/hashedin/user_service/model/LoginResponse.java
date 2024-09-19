package com.hashedin.user_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {

    @Schema(description = " Generated JWT token")
    private String token;
    @Schema(description = " Token expiration time")
    private long expiresIn;

}
