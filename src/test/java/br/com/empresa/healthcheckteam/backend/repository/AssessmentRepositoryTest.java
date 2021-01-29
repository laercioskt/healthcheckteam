package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data2.AnswerOption;
import br.com.empresa.healthcheckteam.backend.data2.Assessment;
import br.com.empresa.healthcheckteam.backend.data2.Assessment.AssessmentBuilder;
import br.com.empresa.healthcheckteam.backend.data2.AssessmentAnswerOption;
import br.com.empresa.healthcheckteam.backend.data2.AssessmentQuestion;
import br.com.empresa.healthcheckteam.backend.data2.Question;
import br.com.empresa.healthcheckteam.backend.data2.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AssessmentRepositoryTest {

    @Autowired
    private AssessmentRepository repository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Test
    public void createASimpleAssessmentWithOneQuestion() {
        Team team = createASimpleTeam();
        Question question = createASimpleQuestion("Question A");
        LocalDate createdDate = LocalDate.now();
        Assessment assessment = new AssessmentBuilder()
                .withCreated(createdDate)
                .withTeam(team)
                .withQuestion(question)
                .build();

        Assessment savedAssessment = repository.save(assessment);

        assertThat(savedAssessment.getId()).isNotNull();
        assertThat(savedAssessment.getCreated()).isEqualTo(createdDate);
        assertThat(savedAssessment.getTeam()).isEqualTo(team);
        assertThat(savedAssessment.getQuestions()).map(AssessmentQuestion::getOrigin).containsExactly(question);
    }

    @Test
    public void createASimpleAssessmentWithOneQuestionAndTwoAnswerOption() {
        Team team = createASimpleTeam();
        Question question = createASimpleQuestion("Question A", "Option 1", "Option 2");
        LocalDate createdDate = LocalDate.now();
        Assessment assessment = new AssessmentBuilder()
                .withCreated(createdDate)
                .withTeam(team)
                .withQuestion(reloadQuestion(question.getId()))
                .build();

        Assessment savedAssessment = repository.save(assessment);

        assertThat(savedAssessment.getQuestions())
                .flatMap(AssessmentQuestion::getOptions)
                .map(AssessmentAnswerOption::getDescription)
                .containsExactlyInAnyOrder("Option 1", "Option 2");
    }

    private Question reloadQuestion(Long questionId) {
        Optional<Question> maybeAQuestion = questionRepository.findById(questionId);
        assertThat(maybeAQuestion).isPresent();
        return maybeAQuestion.get();
    }

    @Test
    public void createASimpleAssessmentWithTwoQuestions() {
        Team team = createASimpleTeam();
        Question questionA = createASimpleQuestion("Question A");
        Question questionB = createASimpleQuestion("Question B");
        LocalDate createdDate = LocalDate.now();
        Assessment assessment = new AssessmentBuilder()
                .withCreated(createdDate)
                .withTeam(team)
                .withQuestion(questionA)
                .withQuestion(questionB)
                .build();

        Assessment savedAssessment = repository.save(assessment);

        assertThat(savedAssessment.getQuestions()).map(AssessmentQuestion::getOrigin)
                .containsExactlyInAnyOrder(questionA, questionB);
    }

    private Question createASimpleQuestion(String description, String... options) {
        Question question = questionRepository.save(new Question.QuestionBuilder().withDescription(description).build());
        stream(options).forEach(option -> createASimpleAnswerOption(option, question));
        return question;
    }

    private void createASimpleAnswerOption(String description, Question question) {
        answerOptionRepository.save(new AnswerOption.AnswerOptionBuilder()
                .withDescription(description)
                .withQuestion(question)
                .build());
    }

    private Team createASimpleTeam() {
        return teamRepository.save(new Team.TeamBuilder().withName("Team A").build());
    }

}