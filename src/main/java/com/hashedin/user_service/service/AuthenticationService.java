package com.hashedin.user_service.service;

import com.hashedin.user_service.model.LoginUser;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.Role;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUser input) {

        User user = User.builder()
                .name(input.getName())
                .email(input.getEmail())
                .address(input.getAddress())
                .role(input.getRole())
                .phone_number(input.getContact_number())
                .password(passwordEncoder.encode(input.getPassword())).build();

        return userRepository.save(user);
    }

    public User authenticate(LoginUser input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        logger.info("User {}  trying to log in.", input.getEmail());
        return userRepository.findByEmail(input.getEmail())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") );
    }
}
