package com.technicaltale.examapi.util.factory;

import com.technicaltale.examapi.entity.EssayQuestion;
import com.technicaltale.examapi.entity.Question;

import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;

public class EssayQuestionFactory extends QuestionFactory {
    private final Faker faker = new Faker();
    @Setter @Getter private int minWords;
   
    public EssayQuestionFactory(int minWords) {
        this.minWords = minWords;
    }
    public EssayQuestionFactory() {
        this.minWords = 10;
    }
   
    @Override
    public Question createQuestion() {
        EssayQuestion q = new EssayQuestion();
        q.setMinWords(this.minWords);
        q.setContent(faker.lorem().paragraph(2));
        return q;
    }
}
