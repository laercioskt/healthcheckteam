package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Answer;
import br.com.empresa.healthcheckteam.backend.data.Assessment;
import br.com.empresa.healthcheckteam.backend.data.Questao;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;

@CssImport("./styles/assessment-form.css")
public class AssessmentsForm extends Div {

    private final VerticalLayout content;

    private Button next;
    private Button discard;
    private Button cancel;
    private final Button previous;

    private final AssessmentsViewLogic viewLogic;
    private final Binder<Assessment> binder;
    private Assessment currentAssessment;

    public AssessmentsForm(AssessmentsViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        Questao questao = DataService.get().getAllQuestoes().iterator().next();
        content.add(new H2(questao.getDescricao()));

        RadioButtonGroup<Answer> sample = new RadioButtonGroup<>();
        sample.setItems(questao.getAnswers());
        sample.setRenderer(new ComponentRenderer<>(item -> {
            Span span = new Span(item.getAnswer());
            span.addClassName("answer-" + item.getOrder());
            return span;
        }));
        content.add(sample);

        binder = new BeanValidationBinder<>(Assessment.class);

        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            next.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        next = new Button("Next");
        next.setWidth("100%");
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        next.addClickListener(event -> {
            if (currentAssessment != null && binder.writeBeanIfValid(currentAssessment)) {
                viewLogic.saveAssessment(currentAssessment);
            }
        });
        next.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editAssessment(currentAssessment));

        previous = new Button("Previous");
        previous.setWidth("100%");
        previous.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        previous.addClickListener(event -> {
            if (currentAssessment != null) {
                viewLogic.deleteAssessment(currentAssessment);
            }
        });

        cancel = new Button("Back to Main");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelAssessment());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelAssessment())
                .setFilter("event.key == 'Escape'");

        content.add(next, previous, cancel);
    }

    public void editAssessment(Assessment assessment) {
        if (assessment == null) {
            assessment = new Assessment();
        }
        previous.setVisible(!assessment.isNewAssessment());
        currentAssessment = assessment;
        binder.readBean(assessment);
    }

}
