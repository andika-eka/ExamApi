package com.technicaltale.examapi.util.factory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import com.technicaltale.examapi.enums.UserRole;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;

public class UserFactory {
    
    @Setter @Getter UserDetailsManager userManager;
    @Setter @Getter PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();

    public UserFactory(){
        this.userManager= new InMemoryUserDetailsManager();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserFactory(UserDetailsManager userManager, PasswordEncoder passwordEncoder){
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails createUser(){
        UserDetails user = User.builder()
                .username(faker.internet().username())
                .password(passwordEncoder.encode(faker.internet().password()))
                .roles(UserRole.USER.name())
                .build();
        userManager.createUser(user);
        return user;
    }

    public UserDetails createAdmin(){
        UserDetails admin = User.builder()
                .username(faker.internet().username())
                .password(passwordEncoder.encode(faker.internet().password()))
                .roles(UserRole.ADMIN.name())
                .build();
        userManager.createUser(admin);
        return admin;
    }


    
    
}
