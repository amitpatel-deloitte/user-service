package com.hashedin.user_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    @Schema(description = " Email of the user")
    private String email;

    @Schema(description = " Password of the user")
    private String password;
}
