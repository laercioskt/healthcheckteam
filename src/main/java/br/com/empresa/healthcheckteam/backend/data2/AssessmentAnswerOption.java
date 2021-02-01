package br.com.empresa.healthcheckteam.backend.data2;

import br.com.empresa.healthcheckteam.backend.data.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Audited
public class AssessmentAnswerOption extends BaseEntity implements Serializable {

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "assessment_question_id", nullable = false)
    private AssessmentQuestion assessmentQuestion;

    @ManyToOne
    @JoinColumn(name = "origin_id", nullable = false)
    private AnswerOption origin;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AssessmentQuestion getAssessmentQuestion() {
        return assessmentQuestion;
    }

    public void setAssessmentQuestion(AssessmentQuestion assessmentQuestion) {
        this.assessmentQuestion = assessmentQuestion;
    }

    public AnswerOption getOrigin() {
        return origin;
    }

    public void setOrigin(AnswerOption origin) {
        this.origin = origin;
    }

    public static class AssessmentAnswerOptionBuilder {

        private String description;
        private AssessmentQuestion assessmentQuestion;
        private AnswerOption origin;

        public AssessmentAnswerOptionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public AssessmentAnswerOptionBuilder withAssessmentQuestion(AssessmentQuestion assessmentQuestion) {
            this.assessmentQuestion = assessmentQuestion;
            return this;
        }

        public AssessmentAnswerOptionBuilder withOrigin(AnswerOption origin) {
            this.origin = origin;
            return this;
        }

        public AssessmentAnswerOption build() {
            AssessmentAnswerOption assessmentAnswerOption = new AssessmentAnswerOption();
            assessmentAnswerOption.setDescription(description);
            assessmentAnswerOption.setAssessmentQuestion(assessmentQuestion);
            assessmentAnswerOption.setOrigin(origin);
            return assessmentAnswerOption;
        }
    }

}
