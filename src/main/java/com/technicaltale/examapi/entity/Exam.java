package com.technicaltale.examapi.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) 
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "max_score")
    private Integer maxScore;

    
    @Column(name = "is_public")
    private boolean isPublic =false;
    
    @Column(nullable = false)
    public String owner;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Exam(UUID id, String title, String description, Integer maxScore, boolean isPublic, String owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.maxScore = maxScore;
        this.isPublic = isPublic;
        this.owner = owner;
        this.questions = new ArrayList<>(); 
    }
    public void addQuestion(Question question) {
        questions.add(question);
        question.setExam(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setExam(null);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}