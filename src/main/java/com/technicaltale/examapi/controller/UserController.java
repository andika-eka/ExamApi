package com.technicaltale.examapi.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.technicaltale.examapi.enums.UserRole;

import jakarta.websocket.server.PathParam;

import com.technicaltale.examapi.entity.dto.UserDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final InMemoryUserDetailsManager userManager;
    private final PasswordEncoder passwordEncoder;

    public UserController(InMemoryUserDetailsManager userDetailsService, PasswordEncoder passwordEncoder) {
        this.userManager = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDetails> getUserDetails(@PathParam("username") String username) {
        UserDetails userDetails = userManager.loadUserByUsername(username);
        if (userDetails != null) {
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDetails> getUserProfile(Principal principal) {
        String username = principal.getName();
        UserDetails userDetails = userManager.loadUserByUsername(username);
        if (userDetails != null) {
            return ResponseEntity.ok(userDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserDetails> updateUserProfile(
        @RequestBody UserDto userDto, 
        Principal principal) {
        String username = principal.getName();
        if (!userManager.userExists(username)) {
            return ResponseEntity.notFound().build();
        }
        UserDetails currentUserDetails = userManager.loadUserByUsername(username);
        User.UserBuilder userBuilder = User.withUserDetails(currentUserDetails);

        if (userDto.password() != null && !userDto.password().isEmpty()) {
            userBuilder.password(passwordEncoder.encode(userDto.password()));
        } else {
            userBuilder.password(currentUserDetails.getPassword());
        }

        UserDetails updatedUserDetails = userBuilder.username(username).build();
        userManager.updateUser(updatedUserDetails);
        return ResponseEntity.ok(updatedUserDetails);

    }

    @PatchMapping("admin/{username}")
    public ResponseEntity<UserDetails> updateUser(
        @PathVariable String username, 
        @RequestBody UserDto userDto, 
        @RequestBody UserRole role) {

        if (!userManager.userExists(username)) {
            return ResponseEntity.notFound().build();
        }
        UserDetails currentUserDetails = userManager.loadUserByUsername(username);
        User.UserBuilder userBuilder = User.withUserDetails(currentUserDetails);

        
        userBuilder.password(currentUserDetails.getPassword());
        if (role != null) {
            userBuilder.roles(role.name());
        } else {
            String[] roles = currentUserDetails.getAuthorities().stream()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .toArray(String[]::new);
            userBuilder.roles(roles);
        }

        UserDetails updatedUserDetails = userBuilder.username(username).build();
        userManager.updateUser(updatedUserDetails);
        return ResponseEntity.ok(updatedUserDetails);

    }

    @DeleteMapping("admin/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (userManager.userExists(username)) {
            userManager.deleteUser(username);
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.notFound().build();
    }



    

}
