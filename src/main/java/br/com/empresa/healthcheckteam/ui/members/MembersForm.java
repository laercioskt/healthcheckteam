package br.com.empresa.healthcheckteam.ui.members;

import br.com.empresa.healthcheckteam.backend.data.Member;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A form for editing a single member.
 */
public class MembersForm extends Div {

    private final VerticalLayout content;

    private final TextField name;
    private final TextField username;
    private final PasswordField password;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final MembersViewLogic viewLogic;
    private final Binder<Member> binder;
    private Member currentMember;

    public MembersForm(MembersViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        name = new TextField("Member name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        username = new TextField("Member user");
        username.setWidth("100%");
        username.setRequired(true);
        username.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(username);

        password = new PasswordField("Member password");
        password.setWidth("100%");
        password.setRequired(true);
        password.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(password);

        binder = new BeanValidationBinder<>(Member.class);
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
            if (currentMember != null && binder.writeBeanIfValid(currentMember)) {
                viewLogic.saveMember(currentMember);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editMember(currentMember));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelMember());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelMember())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentMember != null) {
                viewLogic.deleteMember(currentMember);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editMember(Member member) {
        if (member == null) {
            member = new Member();
        }
        delete.setVisible(!member.isNew());
        currentMember = member;
        binder.readBean(member);
    }

}
