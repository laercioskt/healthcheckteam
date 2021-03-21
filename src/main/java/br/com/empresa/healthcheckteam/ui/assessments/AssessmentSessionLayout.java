package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.data.Assessment;
import br.com.empresa.healthcheckteam.backend.data.AssessmentAnswerOption;
import br.com.empresa.healthcheckteam.backend.data.AssessmentQuestion;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class AssessmentSessionLayout extends VerticalLayout {

    private Button next;
    private Button discard;
    private Button done;
    private final Button previous;

    private final Binder<Assessment> binder;

//    private final AssessmentsViewLogic viewLogic;

    private AssessmentQuestion currentQuestion;

    private AssessmentLayoutListener nextListener;
    private AssessmentLayoutListener previousListener;
    private AssessmentLayoutListener doneListener;

    public AssessmentSessionLayout(AssessmentQuestion question, AssessmentSessionViewLogic sampleCrudLogic, boolean hasPrevious, boolean hasNext) {
        currentQuestion = question;

        setSizeUndefined();
        addClassName("register-form-content");

//        viewLogic = sampleCrudLogic;

        add(new H2(question.getDescription()));

        RadioButtonGroup<AssessmentAnswerOption> options = new RadioButtonGroup<>();
        options.setItems(question.getOptions());
        options.setRenderer(new ComponentRenderer<>(item -> {
            Span span = new Span(item.getDescription());
            // TODO to color
            // span.addClassName("answer-" + item.getOrder());
            return span;
        }));
        add(options);

        binder = new BeanValidationBinder<>(Assessment.class);

        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            this.next.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        this.next = new Button("Next");
        this.next.setWidth("100%");
        this.next.setEnabled(hasNext);
        this.next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.next.addClickListener(event -> {
//            if (currentQuestao != null && binder.writeBeanIfValid(currentQuestao)) {
//                viewLogic.saveAssessment(currentQuestao);
//            }
            nextListener.action();
        });
        this.next.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
//        discard.addClickListener(event -> viewLogic.editAssessment(currentQuestao));

        previous = new Button("Previous");
        previous.setWidth("100%");
        previous.setEnabled(hasPrevious);
        previous.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        previous.addClickListener(event -> {
//            if (currentQuestao != null) {
////                viewLogic.deleteAssessment(currentQuestao);
//            }
            previousListener.action();
        });

        done = new Button("Back to Main");
        done.setWidth("100%");
        done.addClickListener(event -> {
//            viewLogic.cancelAssessment()
            doneListener.action();
        });
        done.addClickShortcut(Key.ESCAPE);
//        getElement()
//                .addEventListener("keydown", event -> viewLogic.cancelAssessment())
//                .setFilter("event.key == 'Escape'");

        add(this.next, previous, done);

    }

    public void addNextListener(AssessmentLayoutListener next) {
        nextListener = next;
//        next.action();
    }

    public void addPreviousListener(AssessmentLayoutListener previous) {
        previousListener = previous;
//        previous.action();
    }

    public void addDoneListener(AssessmentLayoutListener done) {
        doneListener = done;
    }

    @FunctionalInterface
    interface AssessmentLayoutListener {

        void action();

    }

}
