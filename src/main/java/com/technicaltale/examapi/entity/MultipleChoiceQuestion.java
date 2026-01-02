package com.technicaltale.examapi.entity;

import com.technicaltale.examapi.enums.QuestionType;

import com.technicaltale.examapi.converter.StringListConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(QuestionType.MC_VALUE)
public class MultipleChoiceQuestion extends Question {

    // Converts Java List -> JSON String in DB
    @Convert(converter = StringListConverter.class)
    private List<String> options;

    private String correctOption; 

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }
}