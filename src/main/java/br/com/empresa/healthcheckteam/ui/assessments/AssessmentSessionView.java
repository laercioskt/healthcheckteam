package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.data.Assessment;
import br.com.empresa.healthcheckteam.backend.repository.AssessmentRepository;
import br.com.empresa.healthcheckteam.ui.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

/**
 * A view for performing create-read-update-delete operations on 'assessment'.
 * <p>
 * See also {@link AssessmentSessionViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "AssessmentsView", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AssessmentSessionView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Assmt. Sessions";

    private final VerticalLayout grid;
    private final AssessmentSessionForm form;
    private TextField filter;

    private final AssessmentSessionViewLogic viewLogic;
//    private Button newAssessment;

    private final AssessmentSessionDataProvider dataProvider;
    private AssessmentRepository assessmentRepository;

    public AssessmentSessionView(AssessmentRepository assessmentRepository) {
        viewLogic = new AssessmentSessionViewLogic(assessmentRepository, this);
        dataProvider = new AssessmentSessionDataProvider(assessmentRepository);
        this.assessmentRepository = assessmentRepository;

        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new VerticalLayout();
        grid.setWidthFull();
        grid.setPadding(true);
//        grid.add(getAvaliacaoItem("Nova Avaliação", null));
        assessmentRepository.findAll().forEach(assessment -> {
            grid.add(getAvaliacaoItem(assessment.getDescription(), assessment));
        });

        form = new AssessmentSessionForm(assessmentRepository, viewLogic);
        final VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    private Button getAvaliacaoItem(String text, final Assessment assessment) {
        Button h1 = new Button(text);
        h1.setWidthFull();
        h1.setHeight("5em");
        h1.addClickListener(buttonClickEvent -> {
            if (assessment != null) {
                viewLogic.rowSelected(assessment);
            } else {
                viewLogic.newAssessment();
            }
        });
        return h1;
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter by assessment description");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> {
//                    dataProvider.setFilter(event.getValue())
                    grid.removeAll();
                    assessmentRepository.findAll().stream()
                            .filter(a -> a.getDescription().toUpperCase().contains(event.getValue().toUpperCase()))
                            .forEach(assessment -> grid.add(getAvaliacaoItem(assessment.getDescription(), assessment)));
                });


        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

//        newAssessment = new Button("New assessment");
//        // Setting theme variant of new assessmention button to LUMO_PRIMARY that
//        // changes its background color to blue and its text color to white
//        newAssessment.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        newAssessment.setIcon(VaadinIcon.PLUS_CIRCLE.create());
//        newAssessment.addClickListener(click -> viewLogic.newAssessment());
//        // A shortcut to click the new assessment button by pressing ALT + N
//        newAssessment.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
//        topLayout.add(newAssessment);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    /**
     * Shows a temporary popup notification to the user.
     *
     * @param msg
     * @see Notification#show(String)
     */
    public void showNotification(String msg) {
        Notification.show(msg);
    }

    /**
     * Enables/Disables the new assessment button.
     *
     * @param enabled
     */
    public void setNewAssessmentEnabled(boolean enabled) {
//        newAssessment.setEnabled(enabled);
    }

    /**
     * Deselects the selected row in the grid.
     */
    public void clearSelection() {
//        grid.getSelectionModel().deselectAll();
    }

    /**
     * Selects a row
     *
     * @param row
     */
    public void selectRow(Assessment row) {
//        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a assessment in the list of assessments.
     *
     * @param assessment
     */
    public void updateAssessment(Assessment assessment) {
        dataProvider.save(assessment);
    }

    /**
     * Removes a assessment from the list of assessments.
     *
     * @param assessment
     */
    public void removeAssessment(Assessment assessment) {
        dataProvider.delete(assessment);
    }

    /**
     * Displays user a form to edit a Assessment.
     *
     * @param assessment
     */
    public void editAssessment(Assessment assessment) {
        showForm(assessment != null);
        form.editAssessment(assessment);
    }

    /**
     * Shows and hides the new assessment form
     *
     * @param show
     */
    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }

}
