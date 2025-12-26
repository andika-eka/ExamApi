package com.technicaltale.examapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technicaltale.examapi.entity.Exam;

import java.util.UUID;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    List<Exam> findByTitleContainingIgnoreCase(String keyword);
}
