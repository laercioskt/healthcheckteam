package br.com.empresa.healthcheckteam.ui.answers;

import br.com.empresa.healthcheckteam.backend.data.Answer;
import br.com.empresa.healthcheckteam.ui.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

/**
 * A view for performing create-read-update-delete operations on 'answer'.
 * <p>
 * See also {@link AnswersViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Answers", layout = MainLayout.class)
@RouteAlias(value = "Answers", layout = MainLayout.class)
public class AnswersView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Answers";

    private final AnswersGrid grid;
    private final AnswersForm form;
    private TextField filter;

    private final AnswersViewLogic viewLogic = new AnswersViewLogic(this);
    private Button newAnswer;

    private final AnswersDataProvider dataProvider = new AnswersDataProvider();

    public AnswersView() {
        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new AnswersGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new AnswersForm(viewLogic);
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

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter by answer name");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newAnswer = new Button("New Answer");
        // Setting theme variant of new production button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newAnswer.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAnswer.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newAnswer.addClickListener(click -> viewLogic.newAnswer());
        // A shortcut to click the new answer button by pressing ALT + N
        newAnswer.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newAnswer);
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
     * Enables/Disables the new answer button.
     *
     * @param enabled
     */
    public void setNewAnswerEnabled(boolean enabled) {
        newAnswer.setEnabled(enabled);
    }

    /**
     * Deselects the selected row in the grid.
     */
    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    /**
     * Selects a row
     *
     * @param row
     */
    public void selectRow(Answer row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a answer in the list of answers.
     *
     * @param answer
     */
    public void updateAnswer(Answer answer) {
        dataProvider.save(answer);
    }

    /**
     * Removes a answer from the list of answers.
     *
     * @param answer
     */
    public void removeAnswer(Answer answer) {
        dataProvider.delete(answer);
    }

    /**
     * Displays user a form to edit a Answer.
     *
     * @param answer
     */
    public void editAnswer(Answer answer) {
        showForm(answer != null);
        form.editAnswer(answer);
    }

    /**
     * Shows and hides the new answer form
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
