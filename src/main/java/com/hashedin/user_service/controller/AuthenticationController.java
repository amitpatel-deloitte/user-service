package com.hashedin.user_service.controller;

import com.hashedin.user_service.jwt.auth.JwtService;
import com.hashedin.user_service.model.LoginResponse;
import com.hashedin.user_service.model.LoginUser;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.service.AuthenticationService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    private final List<String> roles = Arrays.asList("ADMIN", "CUSTOMER", "OWNER");

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUser registerUser) throws BadRequestException {
        if(!roles.contains(registerUser.getRole())){
            throw new BadRequestException(" Role can only be in { ADMIN, CUSTOMER, OWNER }");
        }
        User registeredUser = authenticationService.signup(registerUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUser loginUser) {
        User authenticatedUser = authenticationService.authenticate(loginUser);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }
}
