package com.technicaltale.examapi;

import org.assertj.core.api.Assertions;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.technicaltale.examapi.entity.EssayQuestion;
import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.entity.MultipleChoiceQuestion;
import com.technicaltale.examapi.repository.ExamRepository;

@SpringBootTest
@Import(TestcontainersConfiguration.class) // Use the Real Docker DB
@SuppressWarnings("null")
class ExamIntegrationTest {

    @Autowired
    private ExamRepository examRepository;

    @Test
    @Transactional
    void shouldSavePolymorphicQuestions() throws Exception {
        Exam exam = new Exam();
        exam.setTitle("Mixed Exam");
        exam.setOwner("andika");
        

        MultipleChoiceQuestion mc = new MultipleChoiceQuestion();
        mc.setContent("Capital of France?");
        mc.setOptions(List.of("London", "Paris", "Berlin"));
        mc.setCorrectOption(1);
        mc.setAttachmentUri(new URI("http://cdn.com/map.jpg")); 
        exam.addQuestion(mc);

        EssayQuestion essay = new EssayQuestion();
        essay.setContent("Describe the French Revolution.");
        essay.setMinWords(500);
        exam.addQuestion(essay);

        examRepository.save(exam);

        List<Exam> exams = examRepository.findAll();
        Assertions.assertThat(exams).isNotEmpty();
        Exam fetchedExam = exams.stream()
                                .filter(e -> e.getTitle().equals("Mixed Exam"))
                                .findFirst()
                                .orElseThrow(() -> new AssertionError("Mixed Exam not found"));

        Assertions.assertThat(fetchedExam.getQuestions()).hasSize(2);

        Exam foundExam = examRepository.findById(fetchedExam.getId()).orElseThrow();
        Assertions.assertThat(foundExam.getQuestions()).hasSize(2);
        Assertions.assertThat(foundExam.getQuestions().get(0)).isInstanceOf(MultipleChoiceQuestion.class);
        Assertions.assertThat(foundExam.getQuestions().get(1)).isInstanceOf(EssayQuestion.class);

    }

    @Test
    @Transactional
    void shouldSaveExamWithNoQuestions() throws Exception{
        Exam exam = new Exam();
        exam.setTitle("No Questions Exam");
        exam.setOwner("andika");
        examRepository.save(exam);

        List<Exam> exams = examRepository.findAll();
        Assertions.assertThat(exams).isNotEmpty();
        Exam fetchedExam = exams.stream()
                                .filter(e -> e.getTitle().equals("No Questions Exam"))
                                .findFirst()
                                .orElseThrow(() -> new AssertionError("No Questions Exam not found"));

        Assertions.assertThat(fetchedExam.getQuestions()).isEmpty();
        
        Exam updatedExam = examRepository.findById(fetchedExam.getId()).orElseThrow();
        updatedExam.setTitle("Updated exam");
         MultipleChoiceQuestion mc = new MultipleChoiceQuestion();
        mc.setContent("Capital of France?");
        mc.setOptions(List.of("London", "Paris", "Berlin"));
        mc.setCorrectOption(1);
        mc.setAttachmentUri(new URI("http://cdn.com/map.jpg")); 
        updatedExam.addQuestion(mc);

        EssayQuestion essay = new EssayQuestion();
        essay.setContent("Describe the French Revolution.");
        essay.setMinWords(500);
        updatedExam.addQuestion(essay);
        examRepository.save(updatedExam);

        Assertions.assertThat(examRepository.findAll().size()).isEqualTo(exams.size());
        updatedExam = examRepository.findById(fetchedExam.getId()).orElseThrow();
        Assertions.assertThat(updatedExam.getQuestions()).hasSize(2);
        Assertions.assertThat(updatedExam.getQuestions().get(0)).isInstanceOf(MultipleChoiceQuestion.class);
        Assertions.assertThat(updatedExam.getQuestions().get(1)).isInstanceOf(EssayQuestion.class);

    }
    
}