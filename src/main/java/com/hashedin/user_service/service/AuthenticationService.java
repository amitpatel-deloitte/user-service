package com.hashedin.user_service.service;

import com.hashedin.user_service.exception.handler.UserAlreadyExists;
import com.hashedin.user_service.exception.handler.UserNotFoundException;
import com.hashedin.user_service.model.LoginUser;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.Role;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new UserAlreadyExists(" Email already found ");
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
                .orElseThrow( () -> new UserNotFoundException( "User not found"));

        logger.info("User {} logged in.", input.getEmail());

        return user;
    }
}
