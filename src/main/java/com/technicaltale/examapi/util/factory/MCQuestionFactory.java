package com.technicaltale.examapi.util.factory;

import java.util.ArrayList;
import java.util.List;

import com.technicaltale.examapi.entity.MultipleChoiceQuestion;
import com.technicaltale.examapi.entity.Question;
import com.technicaltale.examapi.util.Hashing;

import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;


public class MCQuestionFactory extends QuestionFactory{
    private final Faker faker = new Faker();
    @Setter @Getter private int optionNumber;
    private Hashing hashing;


    public MCQuestionFactory() {
        this.optionNumber = 4;
        this.hashing = new Hashing(this.appkey);
    }

    public MCQuestionFactory(int optionNumber) {
        this.optionNumber = optionNumber;
    }

    @Override
    public Question createQuestion() {
        MultipleChoiceQuestion q = new MultipleChoiceQuestion();
        q.setContent(faker.lorem().paragraph(2));
        List <String> options = new ArrayList<>();
        options.add(faker.lorem().word());
        for(int i = 0 ; i < this.optionNumber ; i++){
            options.add(faker.lorem().sentence());
        }
        q.setOptions(options);
        q.setCorrectOption(hashing.getSecretIndex(q.getContent(), optionNumber));
        return q;
    }


}
