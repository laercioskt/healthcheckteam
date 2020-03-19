package br.com.empresa.healthcheckteam.ui.questoes;

import br.com.empresa.healthcheckteam.backend.data.Questao;
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
 * A view for performing create-read-update-delete operations on 'questao'.
 * <p>
 * See also {@link QuestoesViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Questoes", layout = MainLayout.class)
@RouteAlias(value = "Questoes", layout = MainLayout.class)
public class QuestoesView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Questions";

    private final QuestoesGrid grid;
    private final QuestoesForm form;
    private TextField filter;

    private final QuestoesViewLogic viewLogic = new QuestoesViewLogic(this);
    private Button newQuestao;

    private final QuestoesDataProvider dataProvider = new QuestoesDataProvider();

    public QuestoesView() {
        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new QuestoesGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new QuestoesForm(viewLogic);
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
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newQuestao = new Button("New Question");
        // Setting theme variant of new production button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newQuestao.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newQuestao.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newQuestao.addClickListener(click -> viewLogic.newQuestao());
        // A shortcut to click the new questao button by pressing ALT + N
        newQuestao.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newQuestao);
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
     * Enables/Disables the new questao button.
     *
     * @param enabled
     */
    public void setNewQuestaoEnabled(boolean enabled) {
        newQuestao.setEnabled(enabled);
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
    public void selectRow(Questao row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a questao in the list of questoes.
     *
     * @param questao
     */
    public void updateQuestao(Questao questao) {
        dataProvider.save(questao);
    }

    /**
     * Removes a questao from the list of questoes.
     *
     * @param questao
     */
    public void removeQuestao(Questao questao) {
        dataProvider.delete(questao);
    }

    /**
     * Displays user a form to edit a Questao.
     *
     * @param questao
     */
    public void editQuestao(Questao questao) {
        showForm(questao != null);
        form.editQuestao(questao);
    }

    /**
     * Shows and hides the new questao form
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
