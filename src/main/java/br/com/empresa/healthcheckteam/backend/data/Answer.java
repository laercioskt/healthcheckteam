package br.com.empresa.healthcheckteam.backend.data;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Audited
public class Answer extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "assessment_answer_option_id", nullable = false)
    private AssessmentAnswerOption assessmentAnswerOption;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public AssessmentAnswerOption getAssessmentAnswerOption() {
        return assessmentAnswerOption;
    }

    public void setAssessmentAnswerOption(AssessmentAnswerOption assessmentAnswerOption) {
        this.assessmentAnswerOption = assessmentAnswerOption;
    }

    public static class AnswerBuilder {

        private Member member;
        private AssessmentAnswerOption assessmentAnswerOption;

        public AnswerBuilder withMember(Member member) {
            this.member = member;
            return this;
        }

        public void withAssessmentAnswerOption(AssessmentAnswerOption assessmentAnswerOption) {
            this.assessmentAnswerOption = assessmentAnswerOption;
        }

        public Answer build() {
            Answer answer = new Answer();
            answer.setMember(member);
            answer.setAssessmentAnswerOption(assessmentAnswerOption);
            return answer;
        }
    }

}
