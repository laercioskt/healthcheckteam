package br.com.empresa.healthcheckteam.ui.times;

import br.com.empresa.healthcheckteam.backend.data.Time;
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
 * A form for editing a single time.
 */
public class TimesForm extends Div {

    private final VerticalLayout content;

    private final TextField timeName;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final TimesViewLogic viewLogic;
    private final Binder<Time> binder;
    private Time currentTime;

    public TimesForm(TimesViewLogic sampleCrudLogic) {
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        timeName = new TextField("Team name");
        timeName.setWidth("100%");
        timeName.setRequired(true);
        timeName.setValueChangeMode(ValueChangeMode.EAGER);
        content.add(timeName);

        binder = new BeanValidationBinder<>(Time.class);
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
            if (currentTime != null && binder.writeBeanIfValid(currentTime)) {
                viewLogic.saveTime(currentTime);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editTime(currentTime));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelTime());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelTime())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentTime != null) {
                viewLogic.deleteTime(currentTime);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editTime(Time time) {
        if (time == null) {
            time = new Time();
        }
        delete.setVisible(!time.isNewTime());
        currentTime = time;
        binder.readBean(time);
    }

}
