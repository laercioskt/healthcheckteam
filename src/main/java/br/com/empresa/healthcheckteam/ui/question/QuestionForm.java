package br.com.empresa.healthcheckteam.ui.question;

import br.com.empresa.healthcheckteam.backend.data2.AnswerOption;
import br.com.empresa.healthcheckteam.backend.data2.Question;
import br.com.empresa.healthcheckteam.ui.ConfirmDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.ironlist.IronList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A form for editing a single question.
 */
public class QuestionForm extends Div {

    private final VerticalLayout content;

    private final TextArea description;
    private final IronList<AnswerOption> answers;
    private final Button newAnswerButton;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final QuestionViewLogic viewLogic;
    private final Binder<Question> binder;
    private Question currentQuestion;

    public QuestionForm(QuestionViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        description = new TextArea("Question description");
        description.setWidth("100%");
        description.setRequired(true);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(description);

        newAnswerButton = new Button("Add New Answer");
        newAnswerButton.setDisableOnClick(true);
        content.add(newAnswerButton);

        answers = new IronList<>();
        answers.setRenderer(new ComponentRenderer<>(this::createAnswerEditor));
        content.add(answers);

        newAnswerButton.addClickListener(event -> {
            final AnswerOption answer = new AnswerOption();
            answer.setQuestion(currentQuestion);
            currentQuestion.getOptions().add(answer);
            answers.setItems(currentQuestion.getOptions());
        });

        binder = new BeanValidationBinder<>(Question.class);
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
            if (currentQuestion != null && binder.writeBeanIfValid(currentQuestion)) {
                viewLogic.saveQuestion(currentQuestion);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editQuestion(currentQuestion));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelQuestion());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelQuestion())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentQuestion != null) {
                viewLogic.deleteQuestion(currentQuestion);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    private Component createAnswerEditor(AnswerOption answer) {
        final TextArea answerField = new TextArea();
        answerField.setWidthFull();
        if (answer.getId() < 0) {
            answerField.focus();
        }

        final Button deleteButton = new Button(VaadinIcon.MINUS_CIRCLE_O.create(), event -> {
            // Ask for confirmation before deleting stuff
            final ConfirmDialog dialog = new ConfirmDialog(
                    "Please confirm",
                    "Are you sure you want to delete the answer?",
                    "Delete", () -> {
                currentQuestion.getOptions().remove(answer);
                answers.setItems(currentQuestion.getOptions());
                Notification.show("Answer Deleted.");
                save.setEnabled(true);
                discard.setEnabled(true);
            });

            dialog.open();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        final BeanValidationBinder<AnswerOption> answerBinder = new BeanValidationBinder<>(AnswerOption.class);
        answerBinder.forField(answerField).bind("description");
        answerBinder.setBean(answer);
        answerBinder.addValueChangeListener(event -> {
            if (answerBinder.isValid()) {
                deleteButton.setEnabled(true);
                newAnswerButton.setEnabled(true);
                Notification.show("Answer Saved.");
                save.setEnabled(true);
                discard.setEnabled(true);
            }
        });
        deleteButton.setEnabled(answer.getId() > 0);

        final HorizontalLayout layout = new HorizontalLayout(answerField, deleteButton);
        layout.setFlexGrow(1);
        return layout;
    }

    public void editQuestion(Question question) {
        if (question == null) {
            question = new Question();
        }
        delete.setVisible(!question.isNew());
        currentQuestion = question;
        binder.readBean(question);

        answers.setItems(question.getOptions());
    }

}
