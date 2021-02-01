package br.com.empresa.healthcheckteam.backend.data2;

import br.com.empresa.healthcheckteam.backend.data.BaseEntity;
import br.com.empresa.healthcheckteam.backend.data2.AnswerOption.AnswerOptionBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Audited
public class Question extends BaseEntity implements Serializable {

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "question", cascade = ALL)
    private Set<AnswerOption> options = new HashSet<>();

    @OneToMany(mappedBy = "origin")
    private Set<AssessmentQuestion> assessmentQuestions = new HashSet<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AnswerOption> getOptions() {
        return options;
    }

    public void setOptions(Set<AnswerOption> options) {
        this.options = options;
    }

    public Set<AssessmentQuestion> getAssessmentQuestions() {
        return assessmentQuestions;
    }

    public static class QuestionBuilder {

        private Long id;

        private String description;
        private List<String> answerOption = new ArrayList<>();

        public QuestionBuilder() {
        }

        public QuestionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public QuestionBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public QuestionBuilder withAnswerOption(String answerOption) {
            this.answerOption.add(answerOption);
            return this;
        }

        public Question build() {
            Question question = new Question();
            if (id != null)
                question.setId(id);
            question.setDescription(this.description);
            answerOption.forEach(ao ->
                    question.getOptions()
                            .add(new AnswerOptionBuilder().withQuestion(question).withDescription(ao).build()));
            return question;
        }
    }

}