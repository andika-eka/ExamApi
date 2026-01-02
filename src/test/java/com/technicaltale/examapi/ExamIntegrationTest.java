package com.technicaltale.examapi;

import com.technicaltale.examapi.entity.EssayQuestion;
import com.technicaltale.examapi.entity.Exam;
import com.technicaltale.examapi.entity.MultipleChoiceQuestion;
// import com.technicaltale.examapi.entity.Question;
import com.technicaltale.examapi.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
@Import(TestcontainersConfiguration.class) // Use the Real Docker DB
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

        assertThat(fetchedExam.getQuestions().get(0)).isInstanceOf(MultipleChoiceQuestion.class);
        assertThat(fetchedExam.getQuestions().get(1)).isInstanceOf(EssayQuestion.class);
        
        
        

    }
}