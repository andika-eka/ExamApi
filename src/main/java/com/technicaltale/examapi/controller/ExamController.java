package com.technicaltale.examapi.controller;

import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.repository.ExamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams") 
public class ExamController {

    private final ExamRepository examRepository;

    public ExamController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @GetMapping
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable UUID id) {
        return examRepository.findById(id)
                .map(exam -> ResponseEntity.ok(exam)) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam, Principal principal) {
        exam.setId(null); 
        Exam savedExam = examRepository.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable UUID id, @RequestBody Exam examDetails) {
        return examRepository.findById(id)
                .map(existingExam -> {
                    existingExam.setTitle(examDetails.getTitle());
                    existingExam.setDescription(examDetails.getDescription());
                    existingExam.setMaxScore(examDetails.getMaxScore());
                    
                    Exam updatedExam = examRepository.save(existingExam);
                    return ResponseEntity.ok(updatedExam);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID id) {
        if (examRepository.existsById(id)) {
            examRepository.deleteById(id);
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public List<Exam> searchExams(@RequestParam String title) {
        return examRepository.findByTitleContainingIgnoreCase(title);
    }
}