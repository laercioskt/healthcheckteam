package br.com.empresa.healthcheckteam.ui.questoes;

import br.com.empresa.healthcheckteam.backend.data.Questao;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A form for editing a single questao.
 */
public class QuestoesForm extends Div {

    private final VerticalLayout content;

    private final TextArea descricao;
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

        descricao = new TextArea("Descrição");
        descricao.setWidth("100%");
        descricao.setRequired(true);
        descricao.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(descricao);

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

    public void editQuestao(Questao questao) {
        if (questao == null) {
            questao = new Questao();
        }
        delete.setVisible(!questao.isNewQuestao());
        currentQuestao = questao;
        binder.readBean(questao);
    }

}
