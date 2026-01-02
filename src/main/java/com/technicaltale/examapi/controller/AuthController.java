package com.technicaltale.examapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technicaltale.examapi.entity.dto.UserDto;
import com.technicaltale.examapi.enums.UserRole;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final InMemoryUserDetailsManager userManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(InMemoryUserDetailsManager userManager, PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        if (userManager.userExists(userDto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        var user = User.builder()
        .username(userDto.username())
        .password(passwordEncoder.encode(userDto.password()))
        .roles(UserRole.USER.name())
        .build();
        userManager.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("user has been created");
    }
}
