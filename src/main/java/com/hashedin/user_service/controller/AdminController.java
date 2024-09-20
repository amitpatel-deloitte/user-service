package com.hashedin.user_service.controller;

import com.hashedin.user_service.model.User;
import com.hashedin.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
@Tag(name = "Admin Control")
public class AdminController {

    @Autowired
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all-users")
    @Operation(summary = "View all Users", description = " To view all the users which may contain ADMIN's, CUSTOMERS and RESTAURANT owners")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    @Operation(summary = " Delete Users", description = " To delete user by email ")
    public String deleteUserByEmail(@RequestParam String email) {
        userService.deleteUserByEmail(email);
        return " User with email " + email + " deleted successfully ";
    }
}
