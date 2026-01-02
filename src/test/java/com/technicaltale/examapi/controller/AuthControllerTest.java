package com.technicaltale.examapi.controller;

import com.technicaltale.examapi.config.SecurityConfig;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicaltale.examapi.entity.dto.UserDto;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

@WebMvcTest(AuthController.class)
@SuppressWarnings("null")
@Import(SecurityConfig.class)
public class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InMemoryUserDetailsManager userManager;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterNewUser() throws Exception {
        UserDto request = new UserDto("newuser", "securepass");
        
        Mockito.when(userManager.userExists("newuser")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("securepass")).thenReturn("encoded_pass");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated()); // expect 201 Created


       Mockito.verify(userManager).userExists("newuser");
       Mockito.verify(userManager).createUser(ArgumentMatchers.any());
       Mockito.verify(passwordEncoder).encode("securepass");
    }

    @Test
    void shouldRejectExistingUser() throws Exception {
        UserDto request = new UserDto("admin", "password");
        
        Mockito.when(userManager.userExists("admin")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict()); // Expect 409 Conflict
        Mockito.verify(userManager).userExists("admin");
        Mockito.verify(userManager, org.mockito.Mockito.never()).createUser(ArgumentMatchers.any());
        Mockito.verify(passwordEncoder, org.mockito.Mockito.never()).encode("password");
    }
    
}
