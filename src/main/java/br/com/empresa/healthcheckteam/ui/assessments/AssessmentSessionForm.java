package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.data2.Assessment;
import br.com.empresa.healthcheckteam.backend.data2.AssessmentQuestion;
import br.com.empresa.healthcheckteam.backend.repository.AssessmentRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.ListIterator;

@CssImport("./styles/assessment-form.css")
public class AssessmentSessionForm extends Div {

    private Button next;
    private Button discard;
    private Button cancel;

    private Assessment currentAssessment;

    private ListIterator<AssessmentQuestion> questoes;
    private AssessmentSessionViewLogic sampleCrudLogic;

    public AssessmentSessionForm(AssessmentRepository assessmentRepository, AssessmentSessionViewLogic sampleCrudLogic) {
        this.sampleCrudLogic = sampleCrudLogic;
        setClassName("register-form");

//        questoes = new ArrayList<>(sampleCrudLogic.getQuestions()).listIterator();
//        if (!questoes.hasNext())
//            throw new IllegalArgumentException("Assessment without questions registered");
//
//        Questao next = questoes.next();
//        boolean hasPrevious = questoes.hasPrevious();
//        boolean hasNext = questoes.hasNext();
//        add(getAssessmentLayout(sampleCrudLogic, next, hasPrevious, hasNext));
//
//        content = new VerticalLayout();
//        content.setSizeUndefined();
//        content.addClassName("register-form-content");
//        add(content);
//
//        viewLogic = sampleCrudLogic;
//
//        Questao questao = new ArrayList<>(DataService.get().getAllQuestoes()).listIterator().next();
//        content.add(new H2(questao.getDescricao()));
//
//        RadioButtonGroup<Answer> sample = new RadioButtonGroup<>();
//        sample.setItems(questao.getAnswers());
//        sample.setRenderer(new ComponentRenderer<>(item -> {
//            Span span = new Span(item.getAnswer());
//            span.addClassName("answer-" + item.getOrder());
//            return span;
//        }));
//        content.add(sample);
//
//        binder = new BeanValidationBinder<>(Assessment.class);
//
//        binder.addStatusChangeListener(event -> {
//            final boolean isValid = !event.hasValidationErrors();
//            final boolean hasChanges = binder.hasChanges();
//            this.next.setEnabled(hasChanges && isValid);
//            discard.setEnabled(hasChanges);
//        });
//
//        this.next = new Button("Next");
//        this.next.setWidth("100%");
//        this.next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        this.next.addClickListener(event -> {
//            if (currentAssessment != null && binder.writeBeanIfValid(currentAssessment)) {
//                viewLogic.saveAssessment(currentAssessment);
//            }
//        });
//        this.next.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);
//
//        discard = new Button("Discard changes");
//        discard.setWidth("100%");
//        discard.addClickListener(event -> viewLogic.editAssessment(currentAssessment));
//
//        previous = new Button("Previous");
//        previous.setWidth("100%");
//        previous.addThemeVariants(ButtonVariant.LUMO_ERROR,
//                ButtonVariant.LUMO_PRIMARY);
//        previous.addClickListener(event -> {
//            if (currentAssessment != null) {
//                viewLogic.deleteAssessment(currentAssessment);
//            }
//        });
//
//        cancel = new Button("Back to Main");
//        cancel.setWidth("100%");
//        cancel.addClickListener(event -> viewLogic.cancelAssessment());
//        cancel.addClickShortcut(Key.ESCAPE);
//        getElement()
//                .addEventListener("keydown", event -> viewLogic.cancelAssessment())
//                .setFilter("event.key == 'Escape'");
//
//        content.add(this.next, previous, cancel);
    }

    private AssessmentSessionLayout getAssessmentLayout(AssessmentSessionViewLogic sampleCrudLogic, AssessmentQuestion questao, boolean hasPrevious, boolean hasNext) {
        AssessmentSessionLayout assessmentSessionLayout = new AssessmentSessionLayout(questao, sampleCrudLogic, hasPrevious, hasNext);

        assessmentSessionLayout.addNextListener(() -> {
            removeAll();
            if (!questoes.hasNext())
                throw new IllegalArgumentException("There is no questions after that");

            AssessmentQuestion next = questoes.next();
            boolean nextHasNext = questoes.hasNext();
            add(getAssessmentLayout(sampleCrudLogic, next, true, nextHasNext));
        });

        assessmentSessionLayout.addPreviousListener(() -> {
            removeAll();
            if (!questoes.hasPrevious())
                throw new IllegalArgumentException("There is no questions before that");

            AssessmentQuestion previous = questoes.previous();
            boolean previousHasPrevious = questoes.hasPrevious();
            add(getAssessmentLayout(sampleCrudLogic, previous, previousHasPrevious, true));
        });

        assessmentSessionLayout.addDoneListener(() -> {
        });

        return assessmentSessionLayout;
    }

    public void editAssessment(Assessment assessment) {
        if (assessment == null) {
            assessment = new Assessment();
        }
//        previous.setVisible(!assessment.isNewAssessment());
        currentAssessment = assessment;
//        binder.readBean(assessment);

        questoes = new ArrayList<>(currentAssessment.getQuestions()).listIterator();
        if (!questoes.hasNext())
            throw new IllegalArgumentException("Assessment without questions registered");

        AssessmentQuestion next = questoes.next();
        boolean hasPrevious = questoes.hasPrevious();
        boolean hasNext = questoes.hasNext();
        add(getAssessmentLayout(sampleCrudLogic, next, hasPrevious, hasNext));
    }

}
