package br.com.empresa.healthcheckteam.ui.questoes;

import br.com.empresa.healthcheckteam.backend.data.Answer;
import br.com.empresa.healthcheckteam.backend.data.Questao;
import br.com.empresa.healthcheckteam.ui.ConfirmDialog;
import br.com.empresa.healthcheckteam.ui.healthcheckteam.ProductForm;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A form for editing a single questao.
 */
public class QuestoesForm extends Div {

    private final VerticalLayout content;

    private final TextArea descricao;
    private final IronList<Answer> answers;
    private final Button newAnswerButton;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final QuestoesViewLogic viewLogic;
    private final Binder<Questao> binder;
    private Questao currentQuestao;

    public QuestoesForm(QuestoesViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        descricao = new TextArea("Question description");
        descricao.setWidth("100%");
        descricao.setRequired(true);
        descricao.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(descricao);

        newAnswerButton = new Button("Add New Answer");
        newAnswerButton.setDisableOnClick(true);
        content.add(newAnswerButton);

        answers = new IronList<>();
        answers.setRenderer(new ComponentRenderer<>(this::createAnswerEditor));
        content.add(answers);

        newAnswerButton.addClickListener(event -> {
            final Answer answer = new Answer();
            currentQuestao.getAnswers().add(answer);
            answers.setItems(currentQuestao.getAnswers());
        });

        binder = new BeanValidationBinder<>(Questao.class);
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
            if (currentQuestao != null && binder.writeBeanIfValid(currentQuestao)) {
                viewLogic.saveQuestao(currentQuestao);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editQuestao(currentQuestao));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelQuestao());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelQuestao())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentQuestao != null) {
                viewLogic.deleteQuestao(currentQuestao);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    private Component createAnswerEditor(Answer answer) {
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
                currentQuestao.getAnswers().remove(answer);
                answers.setItems(currentQuestao.getAnswers());
                Notification.show("Answer Deleted.");
                save.setEnabled(true);
                discard.setEnabled(true);
            });

            dialog.open();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        final BeanValidationBinder<Answer> answerBinder = new BeanValidationBinder<>(Answer.class);
        answerBinder.forField(answerField).bind("answer");
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

    public void editQuestao(Questao questao) {
        if (questao == null) {
            questao = new Questao();
        }
        delete.setVisible(!questao.isNewQuestao());
        currentQuestao = questao;
        binder.readBean(questao);

        answers.setItems(questao.getAnswers());
    }

}
