package com.hashedin.user_service.service;

import com.hashedin.user_service.model.LoginUser;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.Role;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.repository.UserRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleService roleService;

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleService roleService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User signup(RegisterUser input) {

        if(userRepository.existsByEmail(input.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already found");
        }
        Role role = roleService.findByName(input.getRole());
        Set<Role> roleSet = new HashSet<>();
        System.out.println("role is " + role.getName());
        roleSet.add(role);

        User user = User.builder()
                .name(input.getName())
                .email(input.getEmail())
                .address(input.getAddress())
                .roles(roleSet)
                .phone_number(input.getContact_number())
                .password(passwordEncoder.encode(input.getPassword())).build();

        return userRepository.save(user);
    }

    public Set<SimpleGrantedAuthority> getAuthority(User user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach( role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }

    public UserDetails authenticate(LoginUser input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        logger.info("User {} logged in.", input.getEmail());

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return getAuthority(user);
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }
        };
    }
}
