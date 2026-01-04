package com.technicaltale.examapi.entity;

import com.technicaltale.examapi.enums.QuestionType;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Setter;
import lombok.Getter;

@Entity
@DiscriminatorValue(QuestionType.ESSAY_VALUE)
public class EssayQuestion extends Question {

    @Setter @Getter private Integer minWords;

}