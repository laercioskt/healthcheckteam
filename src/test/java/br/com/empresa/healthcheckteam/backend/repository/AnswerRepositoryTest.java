package br.com.empresa.healthcheckteam.backend.repository;

import br.com.empresa.healthcheckteam.backend.data2.Answer;
import br.com.empresa.healthcheckteam.backend.data2.Answer.AnswerBuilder;
import br.com.empresa.healthcheckteam.backend.data2.AnswerOption;
import br.com.empresa.healthcheckteam.backend.data2.Assessment;
import br.com.empresa.healthcheckteam.backend.data2.AssessmentAnswerOption;
import br.com.empresa.healthcheckteam.backend.data2.Member;
import br.com.empresa.healthcheckteam.backend.data2.Question;
import br.com.empresa.healthcheckteam.backend.data2.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Test
    public void createASimpleAnswer() {
        Member member = createASimplesMember();
        Set<AssessmentAnswerOption> assessmentOptions = createASimplesAssessmentAnswerOption();
        AnswerBuilder builder = new AnswerBuilder().withMember(member);
        assessmentOptions.forEach(builder::withAssessmentAnswerOption);
        Answer answer = builder.build();

        repository.save(answer);

        assertThat(answer.getId()).isNotNull();
    }

    private Set<AssessmentAnswerOption> createASimplesAssessmentAnswerOption() {
        Team team = createASimpleTeam();
        Question question = createASimpleQuestion("Question A", "Option 1", "Option 2");
        LocalDate createdDate = LocalDate.now();
        Assessment assessment = new Assessment.AssessmentBuilder()
                .withCreated(createdDate)
                .withTeam(team)
                .withQuestion(reloadQuestion(question.getId()))
                .build();
        Assessment savedAssessment = assessmentRepository.save(assessment);
        return savedAssessment.getQuestions().stream().flatMap(aq -> aq.getOptions().stream()).collect(toSet());
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

    private Question reloadQuestion(Long questionId) {
        Optional<Question> maybeAQuestion = questionRepository.findById(questionId);
        assertThat(maybeAQuestion).isPresent();
        return maybeAQuestion.get();
    }

    private Member createASimplesMember() {
        Team teamA = createASimpleTeam();
        return memberRepository.save(new Member.MemberBuilder()
                .withUsername("user")
                .withPassword("password")
                .withName("Member X")
                .withTeam(teamA)
                .build());
    }

    private Team createASimpleTeam() {
        return teamRepository.save(new Team.TeamBuilder().withName("Team A").build());
    }

}