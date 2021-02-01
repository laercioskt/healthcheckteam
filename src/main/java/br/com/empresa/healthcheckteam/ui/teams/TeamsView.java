package br.com.empresa.healthcheckteam.ui.teams;

import br.com.empresa.healthcheckteam.backend.data.Team;
import br.com.empresa.healthcheckteam.backend.repository.TeamRepository;
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
 * A view for performing create-read-update-delete operations on 'team'.
 * <p>
 * See also {@link TeamsViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Teams", layout = MainLayout.class)
@RouteAlias(value = "Teams", layout = MainLayout.class)
public class TeamsView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Teams";

    private final TeamsGrid grid;
    private final TeamsForm form;
    private TextField filter;

    private final TeamsViewLogic viewLogic; // = new TeamsViewLogic(this);
    private Button newTeam;

    private final TeamsDataProvider dataProvider; // = new TeamsDataProvider();

    public TeamsView(TeamRepository teamRepository) {

        viewLogic = new TeamsViewLogic(teamRepository,this);
        dataProvider = new TeamsDataProvider(teamRepository);

        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new TeamsGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new TeamsForm(viewLogic);
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
        filter.setPlaceholder("Filter by team name");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newTeam = new Button("New team");
        // Setting theme variant of new teamion button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newTeam.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newTeam.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newTeam.addClickListener(click -> viewLogic.newTeam());
        // A shortcut to click the new team button by pressing ALT + N
        newTeam.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newTeam);
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
     * Enables/Disables the new team button.
     *
     * @param enabled
     */
    public void setNewTeamEnabled(boolean enabled) {
        newTeam.setEnabled(enabled);
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
    public void selectRow(Team row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a team in the list of teams.
     *
     * @param team
     */
    public void updateTeam(Team team) {
        dataProvider.save(team);
    }

    /**
     * Removes a team from the list of teams.
     *
     * @param team
     */
    public void removeTeam(Team team) {
        dataProvider.delete(team);
    }

    /**
     * Displays user a form to edit a Team.
     *
     * @param team
     */
    public void editTeam(Team team) {
        showForm(team != null);
        form.editTeam(team);
    }

    /**
     * Shows and hides the new team form
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
