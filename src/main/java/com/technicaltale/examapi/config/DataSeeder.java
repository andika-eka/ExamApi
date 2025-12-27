package com.technicaltale.examapi.config;

import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.repository.ExamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("dev")
public class DataSeeder {

    @SuppressWarnings("null")
    @Bean
    CommandLineRunner initDatabase(ExamRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                System.out.println("Database already seeded. Skipping...");
                return;
            }

            Exam exam1 = new Exam(null, "Math 101", "Basic Algebra", 100);
            Exam exam2 = new Exam(null, "Physics 202", "Thermodynamics", 100);
            Exam exam3 = new Exam(null, "History 101", "World War II", 100);

            repository.saveAll(List.of(exam1, exam2, exam3));
            System.out.println("inserted exams into database.");

            System.out.println(" Testing findByTitleContainingIgnoreCase('math')...");

            repository.findByTitleContainingIgnoreCase("math").forEach(exam -> {
                System.out.println("   Found: " + exam.getTitle() + " | ID: " + exam.getId());
            });
        };
    }
}