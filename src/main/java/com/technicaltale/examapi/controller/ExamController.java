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
        exam.setOwner(principal.getName());
        Exam savedExam = examRepository.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(
            @PathVariable UUID id,
            @RequestBody Exam examDetails,
            Principal principal) {
        var optionalExam = examRepository.findById(id);

        if (optionalExam.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Exam existingExam = optionalExam.get();

        String owner = existingExam.getOwner();
        if (owner == null || !owner.equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existingExam.setTitle(examDetails.getTitle());
        existingExam.setDescription(examDetails.getDescription());
        existingExam.setMaxScore(examDetails.getMaxScore());

        Exam updatedExam = examRepository.save(existingExam);
        return ResponseEntity.ok(updatedExam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID id, Principal principal) {
        if (examRepository.existsById(id)) {
            var optionalExam = examRepository.findById(id).get();
            String owner = optionalExam.getOwner();
            if (owner == null || !owner.equals(principal.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
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