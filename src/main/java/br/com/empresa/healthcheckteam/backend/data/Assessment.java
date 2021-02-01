package br.com.empresa.healthcheckteam.backend.data;

import br.com.empresa.healthcheckteam.backend.data.AssessmentQuestion.AssessmentQuestionBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toSet;

@Entity
@Audited
public class Assessment extends BaseEntity implements Serializable {

    private LocalDate created;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    private Set<AssessmentQuestion> questions = new HashSet<>();

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<AssessmentQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<AssessmentQuestion> questions) {
        this.questions = questions;
    }

    public String getDescription() {
        return format("Team: %s - Date: %s", getTeam().getName(), getCreated().format(ofPattern("dd/MM/yyyy")));
    }

    public static class AssessmentBuilder {

        private LocalDate created;
        private Team team;
        private final Set<Question> questions = new HashSet<>();

        public AssessmentBuilder withCreated(LocalDate created) {
            this.created = created;
            return this;
        }

        public AssessmentBuilder withTeam(Team team) {
            this.team = team;
            return this;
        }

        public AssessmentBuilder withQuestion(Question question) {
            this.questions.add(question);
            return this;
        }

        public Assessment build() {
            Assessment assessment = new Assessment();
            assessment.setCreated(created);
            assessment.setTeam(team);
            assessment.setQuestions(questions.stream().map(q -> createAssessmentQuestion(assessment, q)).collect(toSet()));
            return assessment;
        }

        private AssessmentQuestion createAssessmentQuestion(Assessment assessment, Question question) {
            AssessmentQuestionBuilder builder = new AssessmentQuestionBuilder()
                    .withQuestion(question)
                    .withAssessment(assessment);
            question.getOptions().forEach(builder::withAnswerOption);
            return builder.build();
        }

    }
}
