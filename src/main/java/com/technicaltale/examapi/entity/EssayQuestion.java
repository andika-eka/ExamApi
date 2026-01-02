package com.technicaltale.examapi.entity;

import com.technicaltale.examapi.enums.QuestionType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(QuestionType.ESSAY_VALUE)
public class EssayQuestion extends Question {

    private Integer minWords;

    public Integer getMinWords() {
        return minWords;
    }

    public void setMinWords(Integer minWords) {
        this.minWords = minWords;
    }
}