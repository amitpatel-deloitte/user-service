package com.hashedin.user_service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterUser {

    private String name;
    private String email;
    private String contact_number;
    private String password;
    private String address;
    private String role;
}
