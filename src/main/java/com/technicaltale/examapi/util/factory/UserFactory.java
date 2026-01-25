package com.technicaltale.examapi.util.factory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import lombok.Setter;
import net.datafaker.Faker;

public class UserFactory {
    
    @Setter UserDetailsManager userManager;
    @Setter PasswordEncoder passwordEncoder;
    private final Faker faker = new Faker();

    public UserFactory(){
        this.userManager= new InMemoryUserDetailsManager();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserFactory(UserDetailsManager userManager, PasswordEncoder passwordEncoder){
        this.userManager = userManager;
        this.passwordEncoder = passwordEncoder;
    }

    
    
}
