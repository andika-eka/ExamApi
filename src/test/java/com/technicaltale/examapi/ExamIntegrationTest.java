package com.technicaltale.examapi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.technicaltale.examapi.entity.EssayQuestion;
import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.entity.MultipleChoiceQuestion;
// import com.technicaltale.examapi.entity.Question;
import com.technicaltale.examapi.repository.ExamRepository;

@SpringBootTest
@Import(TestcontainersConfiguration.class) // Use the Real Docker DB
@SuppressWarnings("null")
class ExamIntegrationTest {

    @Autowired
    private ExamRepository examRepository;

    @Test
    @Transactional
    void shouldSavePolymorphicQuestions() {
        Exam exam = new Exam();
        exam.setTitle("Mixed Exam");
        exam.setOwner("andika");
        

        MultipleChoiceQuestion mc = new MultipleChoiceQuestion();
        mc.setContent("Capital of France?");
        mc.setOptions(List.of("London", "Paris", "Berlin"));
        mc.setCorrectOption("Paris");
        mc.setAttachmentUri("http://cdn.com/map.jpg"); 
        exam.addQuestion(mc);

        EssayQuestion essay = new EssayQuestion();
        essay.setContent("Describe the French Revolution.");
        essay.setMinWords(500);
        exam.addQuestion(essay);

        examRepository.save(exam);

        List<Exam> exams = examRepository.findAll();
        assertThat(exams).isNotEmpty();
        Exam fetchedExam = exams.stream()
                                .filter(e -> e.getTitle().equals("Mixed Exam"))
                                .findFirst()
                                .orElseThrow(() -> new AssertionError("Mixed Exam not found"));

        assertThat(fetchedExam.getQuestions()).hasSize(2);

        Exam foundExam = examRepository.findById(fetchedExam.getId()).orElseThrow();
        assertThat(foundExam.getQuestions()).hasSize(2);
        assertThat(foundExam.getQuestions().get(0)).isInstanceOf(MultipleChoiceQuestion.class);
        assertThat(foundExam.getQuestions().get(1)).isInstanceOf(EssayQuestion.class);

    }

    @Test
    @Transactional
    void shouldSaveExamWithNoQuestions() {
        Exam exam = new Exam();
        exam.setTitle("No Questions Exam");
        exam.setOwner("andika");
        examRepository.save(exam);

        List<Exam> exams = examRepository.findAll();
        assertThat(exams).isNotEmpty();
        Exam fetchedExam = exams.stream()
                                .filter(e -> e.getTitle().equals("No Questions Exam"))
                                .findFirst()
                                .orElseThrow(() -> new AssertionError("No Questions Exam not found"));

        assertThat(fetchedExam.getQuestions()).isEmpty();
        
        Exam updatedExam = examRepository.findById(fetchedExam.getId()).orElseThrow();
        updatedExam.setTitle("Updated exam");
         MultipleChoiceQuestion mc = new MultipleChoiceQuestion();
        mc.setContent("Capital of France?");
        mc.setOptions(List.of("London", "Paris", "Berlin"));
        mc.setCorrectOption("Paris");
        mc.setAttachmentUri("http://cdn.com/map.jpg"); 
        updatedExam.addQuestion(mc);

        EssayQuestion essay = new EssayQuestion();
        essay.setContent("Describe the French Revolution.");
        essay.setMinWords(500);
        updatedExam.addQuestion(essay);
        examRepository.save(updatedExam);

        assertThat(examRepository.findAll().size()).isEqualTo(exams.size());
        updatedExam = examRepository.findById(fetchedExam.getId()).orElseThrow();
        assertThat(updatedExam.getQuestions()).hasSize(2);
        assertThat(updatedExam.getQuestions().get(0)).isInstanceOf(MultipleChoiceQuestion.class);
        assertThat(updatedExam.getQuestions().get(1)).isInstanceOf(EssayQuestion.class);

    }
    
}