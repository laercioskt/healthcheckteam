package br.com.empresa.healthcheckteam.ui.assessment;

import br.com.empresa.healthcheckteam.backend.data2.Assessment;
import br.com.empresa.healthcheckteam.backend.data2.AssessmentQuestion;
import br.com.empresa.healthcheckteam.backend.data2.AssessmentQuestion.AssessmentQuestionBuilder;
import br.com.empresa.healthcheckteam.backend.data2.Team;
import br.com.empresa.healthcheckteam.backend.repository.QuestionRepository;
import br.com.empresa.healthcheckteam.backend.repository.TeamRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * A form for editing a single assessment.
 */
public class AssessmentForm extends Div {

    private final VerticalLayout content;

    private final DatePicker created;
    private final ComboBox<Team> team;
    private Button save;
    private Button discard;
    private Button cancel;
    private final Button delete;

    private final AssessmentViewLogic viewLogic;
    private final Binder<Assessment> binder;
    private Assessment currentAssessment;
    private final QuestionRepository questionRepository;

    public AssessmentForm(AssessmentViewLogic sampleCrudLogic, TeamRepository teamRepository, QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        setClassName("register-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("register-form-content");
        add(content);

        viewLogic = sampleCrudLogic;

        created = new DatePicker("Assessment created");
        created.setWidth("100%");
        created.setRequiredIndicatorVisible(true);
        created.setValue(LocalDate.now());
        content.add(created);

        team = new ComboBox<>("Assessment team");
        team.setWidth("100%");
        team.setRequired(true);
        team.setItemLabelGenerator(Team::getName);
        team.setItems((team, filter) -> filter.isEmpty() ||
                team.getName().toUpperCase().contains(filter.toUpperCase()), teamRepository.findAll());
        content.add(team);

        binder = new BeanValidationBinder<>(Assessment.class);
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
            if (currentAssessment != null && binder.writeBeanIfValid(currentAssessment)) {
                Set<AssessmentQuestion> questions = questionRepository.findAll().stream().map(question -> {
                    AssessmentQuestionBuilder builder = new AssessmentQuestionBuilder()
                            .withAssessment(currentAssessment)
                            .withQuestion(question);
                    question.getOptions().forEach(builder::withAnswerOption);
                    return builder.build();
                }).collect(toSet());
                currentAssessment.setQuestions(questions);
                viewLogic.saveAssessment(currentAssessment);
            }
        });
        save.addClickShortcut(Key.KEY_S, KeyModifier.CONTROL);

        discard = new Button("Discard changes");
        discard.setWidth("100%");
        discard.addClickListener(event -> viewLogic.editAssessment(currentAssessment));

        cancel = new Button("Cancel");
        cancel.setWidth("100%");
        cancel.addClickListener(event -> viewLogic.cancelAssessment());
        cancel.addClickShortcut(Key.ESCAPE);
        getElement()
                .addEventListener("keydown", event -> viewLogic.cancelAssessment())
                .setFilter("event.key == 'Escape'");

        delete = new Button("Delete");
        delete.setWidth("100%");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        delete.addClickListener(event -> {
            if (currentAssessment != null) {
                viewLogic.deleteAssessment(currentAssessment);
            }
        });

        content.add(save, discard, delete, cancel);
    }

    public void editAssessment(Assessment assessment) {
        if (assessment == null) {
            assessment = new Assessment();
        }
        delete.setVisible(!assessment.isNew());
        currentAssessment = assessment;
        binder.readBean(assessment);
    }

}
