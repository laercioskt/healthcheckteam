package br.com.empresa.healthcheckteam.ui.times;

import br.com.empresa.healthcheckteam.backend.data.Time;
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
 * A view for performing create-read-update-delete operations on 'time'.
 * <p>
 * See also {@link TimesViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Times", layout = MainLayout.class)
@RouteAlias(value = "Times", layout = MainLayout.class)
public class TimesView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Times";

    private final TimesGrid grid;
    private final TimesForm form;
    private TextField filter;

    private final TimesViewLogic viewLogic = new TimesViewLogic(this);
    private Button newTime;

    private final TimesDataProvider dataProvider = new TimesDataProvider();

    public TimesView() {
        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new TimesGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new TimesForm(viewLogic);
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
        filter.setPlaceholder("Filter name");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newTime = new Button("New time");
        // Setting theme variant of new timeion button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newTime.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newTime.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newTime.addClickListener(click -> viewLogic.newTime());
        // A shortcut to click the new time button by pressing ALT + N
        newTime.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newTime);
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
     * Enables/Disables the new time button.
     *
     * @param enabled
     */
    public void setNewTimeEnabled(boolean enabled) {
        newTime.setEnabled(enabled);
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
    public void selectRow(Time row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a time in the list of times.
     *
     * @param time
     */
    public void updateTime(Time time) {
        dataProvider.save(time);
    }

    /**
     * Removes a time from the list of times.
     *
     * @param time
     */
    public void removeTime(Time time) {
        dataProvider.delete(time);
    }

    /**
     * Displays user a form to edit a Time.
     *
     * @param time
     */
    public void editTime(Time time) {
        showForm(time != null);
        form.editTime(time);
    }

    /**
     * Shows and hides the new time form
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
