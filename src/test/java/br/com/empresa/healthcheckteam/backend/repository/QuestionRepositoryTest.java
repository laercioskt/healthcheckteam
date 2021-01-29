package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data2.Question;
import br.com.empresa.healthcheckteam.backend.data2.Question.QuestionBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository repository;

    @Test
    public void createASimpleQuestion() {
        Question questionA = new QuestionBuilder()
                .withDescription("Question A")
                .build();
        Question savedQuestion = repository.save(questionA);
        assertNotNull(savedQuestion.getId());
    }

}