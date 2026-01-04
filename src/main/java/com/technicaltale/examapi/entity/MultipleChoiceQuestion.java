package com.technicaltale.examapi.entity;

import com.technicaltale.examapi.enums.QuestionType;

import com.technicaltale.examapi.converter.StringListConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@DiscriminatorValue(QuestionType.MC_VALUE)
public class MultipleChoiceQuestion extends Question {

    // Converts Java List -> JSON String in DB
    @Convert(converter = StringListConverter.class)
    @Getter @Setter private List<String> options;
    @Getter @Setter private int correctOption; 

    public String getCorrectOptionString() {
        return options.get(correctOption);
    }

    

}