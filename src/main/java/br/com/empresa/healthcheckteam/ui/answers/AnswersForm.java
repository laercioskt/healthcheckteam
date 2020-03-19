package br.com.empresa.healthcheckteam.ui.answers;

import br.com.empresa.healthcheckteam.backend.data.Answer;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A form for editing a single answer.
 */
public class AnswersForm extends Div {

    private final VerticalLayout content;

    private final TextArea answer;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final AnswersViewLogic viewLogic;
    private final Binder<Answer> binder;
    private Answer currentAnswer;

    public AnswersForm(AnswersViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        answer = new TextArea("Answer description");
        answer.setWidth("100%");
        answer.setRequired(true);
        answer.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(answer);

        binder = new BeanValidationBinder<>(Answer.class);
        binder.bindInstanceFields(this);

        // enable/disable save button while editing
        binder.addStatusChangeListener(event -> {
            final boolean isValid = !event.hasValidationErrors();
            final boolean hasChanges = binder.hasChanges();
            save.setEnabled(hasChanges && isValid);
            discard.setEnabled(hasChanges);
        });

        save = new Button("Save");
        save.setWidth("100%");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> {
            if (currentAnswer != null && binder.writeBeanIfValid(currentAnswer)) {
                viewLogic.saveAnswer(currentAnswer);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editAnswer(currentAnswer));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelAnswer());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelAnswer())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentAnswer != null) {
                viewLogic.deleteAnswer(currentAnswer);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editAnswer(Answer answer) {
        if (answer == null) {
            answer = new Answer();
        }
        delete.setVisible(!answer.isNewAnswer());
        currentAnswer = answer;
        binder.readBean(answer);
    }

}
