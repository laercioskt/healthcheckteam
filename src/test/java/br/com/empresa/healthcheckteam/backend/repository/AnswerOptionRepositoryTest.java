package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data2.AnswerOption;
import br.com.empresa.healthcheckteam.backend.data2.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AnswerOptionRepositoryTest {

    @Autowired
    private AnswerOptionRepository repository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void createASimpleAnswerOption() {
        Question question = createASimpleQuestion();
        AnswerOption answerOption1 = new AnswerOption.AnswerOptionBuilder()
                .withDescription("Option 1")
                .withQuestion(question)
                .build();

        AnswerOption savedAnswerOption = repository.save(answerOption1);

        assertThat(savedAnswerOption.getId()).isNotNull();
        assertThat(savedAnswerOption.getQuestion()).isEqualTo(question);
        assertThat(reloadQuestion(question.getId()).getOptions()).containsExactly(savedAnswerOption);
    }

    private Question reloadQuestion(Long questionId) {
        Optional<Question> maybeAQuestion = questionRepository.findById(questionId);
        assertThat(maybeAQuestion).isPresent();
        return maybeAQuestion.get();
    }

    private Question createASimpleQuestion() {
        return questionRepository.save(new Question.QuestionBuilder().withDescription("Question A").build());
    }

}