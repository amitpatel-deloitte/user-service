package com.hashedin.user_service.controller;

import com.hashedin.user_service.exception.handler.RoleNotAllowed;
import com.hashedin.user_service.jwt.auth.JwtService;
import com.hashedin.user_service.model.LoginResponse;
import com.hashedin.user_service.model.LoginUser;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/auth")
@RestController
@Tag(name = "User Authentication")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    private final List<String> roles = Arrays.asList("ADMIN", "CUSTOMER", "RESTAURANT");

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register Users", description = " To register or create new users ")
    public ResponseEntity<User> register(@RequestBody RegisterUser registerUser) {

        if(!roles.contains(registerUser.getRole())){
            throw new RoleNotAllowed(" Role can only be in { ADMIN, CUSTOMER, RESTAURANT }");
        }
        User registeredUser = authenticationService.signup(registerUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = " Log in ", description = " To login and generate JWT token.")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUser loginUser) {
        UserDetails authenticatedUser = authenticationService.authenticate(loginUser);

        System.out.println( "authorities " + authenticatedUser.getAuthorities());
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }
}
