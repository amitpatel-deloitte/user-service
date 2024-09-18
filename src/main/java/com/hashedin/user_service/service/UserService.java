package com.hashedin.user_service.service;

import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.Role;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        if(!userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteByEmail(email);
    }

    public User updateUserByEmail(String email, RegisterUser user){
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setRole(user.getRole());
        existingUser.setPhone_number(user.getContact_number());
        existingUser.setAddress(user.getAddress());
        existingUser.setUser_id(existingUser.getUser_id());

        return userRepository.save(existingUser);
    }

    public String updatePassword(String email, String password){
        Optional<User> existingUser = userRepository.findByEmail(email);

        if(existingUser.isPresent()){
            User user = existingUser.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "Password update successful for the user " + email;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }
}
