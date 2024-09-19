package com.hashedin.user_service.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {

    private String name;
    private String email;
    private String contact_number;
    private String password;
    private String address;
    private String role;
}
