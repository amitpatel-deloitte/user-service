package com.hashedin.user_service.service;

import com.hashedin.user_service.exception.handler.UserNotFoundException;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.Role;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User findUserById(int id){
        return userRepository.findById(id);
    }

    public List<User> allUsers() {
        System.out.println();
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    //model mapper
    
    @Transactional
    public void deleteUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException( "User not found"));
        existingUser.getRoles().clear();

        userRepository.deleteByEmail(email);
    }

    public User updateUserByEmail(String email, RegisterUser user){
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException( "User not found"));
        Role role = roleService.findByName(user.getRole());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setRoles(roleSet);
        existingUser.setPhone_number(user.getContact_number());
        existingUser.setAddress(user.getAddress());

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
            throw new UserNotFoundException("User not found");
        }
    }
}
