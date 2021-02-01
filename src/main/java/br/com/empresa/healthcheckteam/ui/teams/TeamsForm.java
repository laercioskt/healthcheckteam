package br.com.empresa.healthcheckteam.ui.teams;

import br.com.empresa.healthcheckteam.backend.data2.Team;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A form for editing a single team.
 */
public class TeamsForm extends Div {

    private final VerticalLayout content;

    private final TextField name;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final TeamsViewLogic viewLogic;
    private final Binder<Team> binder;
    private Team currentTeam;

    public TeamsForm(TeamsViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        name = new TextField("Team name");
        name.setWidth("100%");
        name.setRequired(true);
        name.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(name);

        binder = new BeanValidationBinder<>(Team.class);
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
            if (currentTeam != null && binder.writeBeanIfValid(currentTeam)) {
                viewLogic.saveTeam(currentTeam);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editTeam(currentTeam));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelTeam());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelTeam())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentTeam != null) {
                viewLogic.deleteTeam(currentTeam);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editTeam(Team team) {
        if (team == null) {
            team = new Team();
        }
        delete.setVisible(!team.isNew());
        currentTeam = team;
        binder.readBean(team);
    }

}
