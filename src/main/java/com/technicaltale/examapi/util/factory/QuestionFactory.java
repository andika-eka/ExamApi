package com.technicaltale.examapi.util.factory;

import java.util.List;

import com.technicaltale.examapi.entity.Question;

import lombok.Setter;

import com.technicaltale.examapi.entity.Exam;

public abstract class QuestionFactory {

    @Setter protected String appkey;
    public abstract Question createQuestion();

    public List<Question> createQuestions(int count){
        List<Question> questions = List.of();
        for (int i = 0; i < count; i++) {
            questions.add(createQuestion());
        }
        return questions;
    }

    public void addQuestion(Exam exam, int count){
        exam.addQuestions(createQuestions(count));
    }



}
