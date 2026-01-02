package com.technicaltale.examapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ExamController.class) 
@SuppressWarnings("null")
class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExamRepository examRepository; 

    @Autowired
    private ObjectMapper objectMapper; 

    @Test
    @WithMockUser(username = "admin") 
    void shouldReturnAllExams() throws Exception {
        
        Exam exam1 = new Exam();
        exam1.setTitle("Math 101");
        Exam exam2 = new Exam();
        exam2.setTitle("Bio 101");
        
        Mockito.when(examRepository.findAll()).thenReturn(List.of(exam1, exam2));

        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/exams"))
                .andExpect(MockMvcResultMatchers.status().isOk()) 
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2)) 
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Math 101")) 
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Bio 101")); 
    }

    @Test
    @WithMockUser(username = "andika") 
    void shouldCreateExam() throws Exception {
        Exam newExam = new Exam();
        newExam.setTitle("History 202");
        newExam.setDescription("World War II");
        newExam.setMaxScore(100);

        Mockito.when(examRepository.save(ArgumentMatchers.any(Exam.class))).thenAnswer(invocation -> {
            Exam e = invocation.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        mockMvc.perform(MockMvcRequestBuilders.post("/api/exams")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) 
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newExam)))
                .andExpect(MockMvcResultMatchers.status().isCreated()) 
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("History 202"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()); 
    }
}