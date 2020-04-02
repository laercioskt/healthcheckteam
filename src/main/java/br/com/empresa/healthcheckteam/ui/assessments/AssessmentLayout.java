package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.data.Answer;
import br.com.empresa.healthcheckteam.backend.data.Assessment;
import br.com.empresa.healthcheckteam.backend.data.Questao;
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

import java.util.function.Consumer;

public class AssessmentLayout extends VerticalLayout {

    private Button next;
    private Button discard;
    private Button cancel;
    private final Button previous;

    private final Binder<Assessment> binder;

//    private final AssessmentsViewLogic viewLogic;

    private Questao currentQuestao;

    private AssessmentLayoutListener nextListener;
    private AssessmentLayoutListener previousListener;
    private AssessmentLayoutListener doneListener;

    public AssessmentLayout(Questao questao, AssessmentsViewLogic sampleCrudLogic, boolean hasPrevious, boolean hasNext) {
        currentQuestao = questao;

        setSizeUndefined();
        addClassName("register-form-content");

//        viewLogic = sampleCrudLogic;

        add(new H2(questao.getDescricao()));

        RadioButtonGroup<Answer> sample = new RadioButtonGroup<>();
        sample.setItems(questao.getAnswers());
        sample.setRenderer(new ComponentRenderer<>(item -> {
            Span span = new Span(item.getAnswer());
            span.addClassName("answer-" + item.getOrder());
            return span;
        }));
        add(sample);

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

        cancel = new Button("Back to Main");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> {
//            viewLogic.cancelAssessment()
            doneListener.action();
        });
        cancel.addClickShortcut(Key.ESCAPE);
//        getElement()
//                .addEventListener("keydown", event -> viewLogic.cancelAssessment())
//                .setFilter("event.key == 'Escape'");

        add(this.next, previous, cancel);

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
//        done.action();
    }

    @FunctionalInterface
    interface AssessmentLayoutListener {

        void action();

    }

}
