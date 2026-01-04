package com.technicaltale.examapi.util.factory;

import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.repository.ExamRepository;

import lombok.Setter;
import net.datafaker.Faker;

public class ExamFactory {

    private final ExamRepository examRepository;
    private final Faker faker = new Faker();
    @Setter private QuestionFactory questionFactory;
    @Setter private String appkey ="";
    @Setter private String owner = "andika";
    @Setter private int maxScore = 100;
    @Setter private boolean isPublic = true;

    

    public ExamFactory(ExamRepository examRepository) {
        this.examRepository = examRepository;
        this.questionFactory = new MCQuestionFactory();
        questionFactory.setAppkey(appkey);

    }

    public Exam createExamWithQuestions(int questionCount) {
        Exam exam = new Exam(
            null, 
            faker.educator().course(),
            faker.lorem().paragraph(2),
            this.maxScore, 
            this.isPublic,
            this.owner
            );

        populateExamsWithQuestion(exam, questionCount);
        return examRepository.save(exam);
    }

    public void populateExamsWithQuestion( Exam exam, int questionCount){
        this.questionFactory.addQuestion(exam, questionCount);
    }
}
