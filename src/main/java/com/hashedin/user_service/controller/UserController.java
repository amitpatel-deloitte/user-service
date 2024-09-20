package com.hashedin.user_service.controller;

import com.hashedin.user_service.exception.handler.RoleNotAllowed;
import com.hashedin.user_service.exception.handler.UserNotAuthorised;
import com.hashedin.user_service.model.RegisterUser;
import com.hashedin.user_service.model.User;
import com.hashedin.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/users")
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Control")
public class UserController {

    private final UserService userService;

    private final List<String> roles = Arrays.asList("ADMIN", "CUSTOMER", "OWNER");

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = " Current user details", description = " To see details of the logged in user")
    public ResponseEntity<User> authenticatedUser(@PathVariable("id") int id) {
        User currentUser = userService.findUserById(id);
        return ResponseEntity.ok(currentUser);
    }

    @DeleteMapping("/delete")
    @Operation(summary = " Delete Users", description = " To delete user by email ")
    public String deleteUserByEmail(@RequestParam String email)  throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(!currentUser.getEmail().equals(email)){
            throw new BadRequestException(" Can't delete other user account ");
        }
        userService.deleteUserByEmail(email);
        return " User with email " + email + " deleted successfully";
    }

    @PutMapping("/update")
    @Operation(summary = " Update user details ", description = " To update the users details ")
    public ResponseEntity<User> updateUserByEmail(@RequestParam String email, @RequestBody RegisterUser registerUser) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if(!currentUser.getEmail().equals(email)){
            throw new UserNotAuthorised(" Can't update other user account details");
        }

        if(!roles.contains(registerUser.getRole())){
            throw new RoleNotAllowed(" Role can only be in { ADMIN, CUSTOMER, RESTAURANT }");
        }
            User updatedUser = userService.updateUserByEmail(email, registerUser);
            return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/update-password")
    @Operation(summary = " Reset Password ", description = " If user forgets password.. use option to reset it.")
    public ResponseEntity<String> updatePasswordByEmail(@RequestParam String email, @RequestParam String newPassword) throws BadRequestException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if(!currentUser.getEmail().equals(email)){
            throw new UserNotAuthorised(" Can't delete other user account ");
        }
        return ResponseEntity.ok(userService.updatePassword(email, newPassword));
    }

}
