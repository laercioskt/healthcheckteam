package br.com.empresa.healthcheckteam.ui.members;

import br.com.empresa.healthcheckteam.backend.data.Member;
import br.com.empresa.healthcheckteam.backend.repository.MemberRepository;
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
 * A view for performing create-read-update-delete operations on 'member'.
 * <p>
 * See also {@link MembersViewLogic} for fetching the data, the actual CRUD
 * operations and controlling the view based on events from outside.
 */
@Route(value = "Members", layout = MainLayout.class)
@RouteAlias(value = "Members", layout = MainLayout.class)
public class MembersView extends HorizontalLayout implements HasUrlParameter<String> {

    public static final String VIEW_NAME = "Members";

    private final MembersGrid grid;
    private final MembersForm form;
    private TextField filter;

    private final MembersViewLogic viewLogic; // = new MembersViewLogic(this);
    private Button newMember;

    private final MembersDataProvider dataProvider; // = new MembersDataProvider();

    public MembersView(MemberRepository memberRepository) {

        viewLogic = new MembersViewLogic(memberRepository,this);
        dataProvider = new MembersDataProvider(memberRepository);

        // Sets the width and the height of InventoryView to "100%".
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        grid = new MembersGrid();
        grid.setDataProvider(dataProvider);
        // Allows user to select a single row in the grid.
        grid.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new MembersForm(viewLogic);
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
        filter.setPlaceholder("Filter by member name");
        // Apply the filter to grid's data provider. TextField value is never
        filter.addValueChangeListener(
                event -> dataProvider.setFilter(event.getValue()));
        // A shortcut to focus on the textField by pressing ctrl + F
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newMember = new Button("New member");
        // Setting theme variant of new memberion button to LUMO_PRIMARY that
        // changes its background color to blue and its text color to white
        newMember.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newMember.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newMember.addClickListener(click -> viewLogic.newMember());
        // A shortcut to click the new member button by pressing ALT + N
        newMember.addClickShortcut(Key.KEY_N, KeyModifier.ALT);
        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newMember);
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
     * Enables/Disables the new member button.
     *
     * @param enabled
     */
    public void setNewMemberEnabled(boolean enabled) {
        newMember.setEnabled(enabled);
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
    public void selectRow(Member row) {
        grid.getSelectionModel().select(row);
    }

    /**
     * Updates a member in the list of members.
     *
     * @param member
     */
    public void updateMember(Member member) {
        dataProvider.save(member);
    }

    /**
     * Removes a member from the list of members.
     *
     * @param member
     */
    public void removeMember(Member member) {
        dataProvider.delete(member);
    }

    /**
     * Displays user a form to edit a Member.
     *
     * @param member
     */
    public void editMember(Member member) {
        showForm(member != null);
        form.editMember(member);
    }

    /**
     * Shows and hides the new member form
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
