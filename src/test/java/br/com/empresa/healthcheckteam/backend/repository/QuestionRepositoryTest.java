package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data.AnswerOption;
import br.com.empresa.healthcheckteam.backend.data.Question;
import br.com.empresa.healthcheckteam.backend.data.Question.QuestionBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    public void createASimpleQuestionAndAnswerOption() {
        Question questionA = new QuestionBuilder()
                .withDescription("Question A")
                .withAnswerOption("Option 1")
                .build();

        Question savedQuestion = repository.save(questionA);

        assertNotNull(savedQuestion.getId());
        assertThat(savedQuestion.getOptions()).map(AnswerOption::getDescription).containsExactly("Option 1");
    }


}