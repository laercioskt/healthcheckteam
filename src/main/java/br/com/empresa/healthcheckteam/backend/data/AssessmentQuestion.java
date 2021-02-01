package br.com.empresa.healthcheckteam.backend.data;

import br.com.empresa.healthcheckteam.backend.data.AssessmentAnswerOption.AssessmentAnswerOptionBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Entity
@Audited
public class AssessmentQuestion extends BaseEntity implements Serializable {

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private Question origin;

    @OneToMany(mappedBy = "assessmentQuestion", cascade = CascadeType.ALL)
    private Set<AssessmentAnswerOption> options = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Question getOrigin() {
        return origin;
    }

    public void setOrigin(Question origin) {
        this.origin = origin;
    }

    public Set<AssessmentAnswerOption> getOptions() {
        return options;
    }

    public void setOptions(Set<AssessmentAnswerOption> options) {
        this.options = options;
    }

    public static class AssessmentQuestionBuilder {

        private Question question;
        private Assessment assessment;
        private final Set<AnswerOption> options = new HashSet<>();

        public AssessmentQuestionBuilder withQuestion(Question question) {
            this.question = question;
            return this;
        }

        public AssessmentQuestionBuilder withAssessment(Assessment assessment) {
            this.assessment = assessment;
            return this;
        }

        public void withAnswerOption(AnswerOption option) {
            this.options.add(option);
        }

        public AssessmentQuestion build() {
            AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
            assessmentQuestion.setAssessment(assessment);
            assessmentQuestion.setDescription(question.getDescription());
            assessmentQuestion.setOrigin(question);
            assessmentQuestion.setOptions(options.stream().map(o -> new AssessmentAnswerOptionBuilder()
                    .withDescription(o.getDescription())
                    .withAssessmentQuestion(assessmentQuestion)
                    .withOrigin(o)
                    .build()).collect(toSet()));
            return assessmentQuestion;
        }
    }
}
