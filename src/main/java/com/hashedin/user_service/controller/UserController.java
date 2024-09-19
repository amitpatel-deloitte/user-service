package com.hashedin.user_service.controller;

import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/users")
@RestController
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    private final List<String> roles = Arrays.asList("ADMIN", "CUSTOMER", "OWNER");

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUserByEmail(@RequestParam String email){
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok(" User with email " + email + " deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserByEmail(@RequestParam String email, @RequestBody RegisterUser user) throws BadRequestException {
        System.out.println(user.getName());
        if(!roles.contains(user.getRole())){
            throw new BadRequestException(" Role can only be in { ADMIN, CUSTOMER, RESTAURANT }");
        }
            User updatedUser = userService.updateUserByEmail(email, user);
            return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePasswordByEmail(@RequestParam String email, @RequestParam String newPassword){
        return ResponseEntity.ok(userService.updatePassword(email, newPassword));
    }

}
