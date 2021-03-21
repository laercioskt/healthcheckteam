package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.data.Assessment;
import br.com.empresa.healthcheckteam.backend.data.AssessmentQuestion;
import br.com.empresa.healthcheckteam.backend.repository.AssessmentRepository;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./styles/assessment-form.css")
public class AssessmentSessionForm extends Div {

    private Assessment currentAssessment;

    private AssessmentIterator questoes;
    private AssessmentSessionViewLogic sampleCrudLogic;

    public AssessmentSessionForm(AssessmentRepository assessmentRepository,
                                 AssessmentSessionViewLogic sampleCrudLogic) {
        this.sampleCrudLogic = sampleCrudLogic;
        setClassName("register-form");
    }

    private AssessmentSessionLayout getAssessmentLayout(AssessmentSessionViewLogic sampleCrudLogic,
                                                        AssessmentQuestion questao,
                                                        boolean hasPrevious, boolean hasNext) {
        AssessmentSessionLayout layout = new AssessmentSessionLayout(questao, sampleCrudLogic, hasPrevious, hasNext);

        layout.addNextListener(() -> {
            removeAll();
            if (!questoes.hasNext())
                throw new IllegalArgumentException("There is no questions after that");

            AssessmentQuestion next = questoes.next();
            boolean nextHasNext = questoes.hasNext();
            add(getAssessmentLayout(sampleCrudLogic, next, true, nextHasNext));
        });

        layout.addPreviousListener(() -> {
            removeAll();
            if (!questoes.hasPrevious())
                throw new IllegalArgumentException("There is no questions before that");

            AssessmentQuestion previous = questoes.previous();
            boolean previousHasPrevious = questoes.hasPrevious();
            add(getAssessmentLayout(sampleCrudLogic, previous, previousHasPrevious, true));
        });

        layout.addDoneListener(sampleCrudLogic::cancelAssessment);

        return layout;
    }

    public void editAssessment(Assessment assessment) {
        if (assessment == null) {
            assessment = new Assessment();
        }
        currentAssessment = assessment;

        questoes = new AssessmentIterator(currentAssessment.getQuestions());
        if (!questoes.hasNext())
            throw new IllegalArgumentException("Assessment without questions registered");

        AssessmentQuestion next = questoes.next();
        boolean hasPrevious = questoes.hasPrevious();
        boolean hasNext = questoes.hasNext();
        add(getAssessmentLayout(sampleCrudLogic, next, hasPrevious, hasNext));
    }

}
