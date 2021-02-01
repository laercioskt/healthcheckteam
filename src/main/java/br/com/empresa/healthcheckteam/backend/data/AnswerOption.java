package br.com.empresa.healthcheckteam.backend.data;

import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
public class AnswerOption extends BaseEntity implements Serializable {

    @Column(length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @OneToMany(mappedBy = "origin")
    private Set<AssessmentAnswerOption> assessmentAnswerOptions = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<AssessmentAnswerOption> getAssessmentAnswerOptions() {
        return assessmentAnswerOptions;
    }

    public static class AnswerOptionBuilder {

        private String description;
        private Question question;

        public AnswerOptionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public AnswerOptionBuilder withQuestion(Question question) {
            this.question = question;
            return this;
        }

        public AnswerOption build() {
            AnswerOption a = new AnswerOption();
            a.setDescription(description);
            a.setQuestion(question);
            return a;
        }
    }
}
