package br.com.empresa.healthcheckteam.ui.question;

import br.com.empresa.healthcheckteam.backend.data2.Question;
import br.com.empresa.healthcheckteam.backend.repository.QuestionRepository;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

/**
 * A view for performing create-read-update-delete operations on 'question'.
 * <p>
 * See also {@link QuestionViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Questions", layout = MainLayout.class)
@RouteAlias(value = "Questions", layout = MainLayout.class)
public class QuestionView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Questions";

    private final QuestionGrid grid;
    private final QuestionForm form;
    private TextField filter;

    private final QuestionViewLogic viewLogic; // = new QuestionsViewLogic(this);
    private Button newQuestion;

    private final QuestionDataProvider dataProvider; // = new QuestionsDataProvider();

    public QuestionView(QuestionRepository questionRepository) {

        viewLogic = new QuestionViewLogic(questionRepository, this);
        dataProvider = new QuestionDataProvider(questionRepository);

        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new QuestionGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new QuestionForm(viewLogic);
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
        filter.setPlaceholder("Filter by question name");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newQuestion = new Button("New question");
        // Setting theme variant of new questionion button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newQuestion.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newQuestion.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newQuestion.addClickListener(click -> viewLogic.newQuestion());
        // A shortcut to click the new question button by pressing ALT + N
        newQuestion.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newQuestion);
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
     * Enables/Disables the new question button.
     *
     * @param enabled
     */
    public void setNewQuestionEnabled(boolean enabled) {
        newQuestion.setEnabled(enabled);
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
    public void selectRow(Question row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a question in the list of questions.
     *
     * @param question
     */
    public void updateQuestion(Question question) {
        dataProvider.save(question);
    }

    /**
     * Removes a question from the list of questions.
     *
     * @param question
     */
    public void removeQuestion(Question question) {
        dataProvider.delete(question);
    }

    /**
     * Displays user a form to edit a Question.
     *
     * @param question
     */
    public void editQuestion(Question question) {
        showForm(question != null);
        form.editQuestion(question);
    }

    /**
     * Shows and hides the new question form
     *
     * @param show
     */
    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
