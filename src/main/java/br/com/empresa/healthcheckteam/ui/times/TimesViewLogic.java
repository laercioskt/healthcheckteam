package br.com.empresa.healthcheckteam.ui.times;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Time;
import com.vaadin.flow.component.UI;

import java.io.Serializable;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the time editor form and the data source, including
 * fetching and saving times.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class TimesViewLogic implements Serializable {

    private final TimesView view;

    public TimesViewLogic(TimesView simpleCrudView) {
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewTimeEnabled(false);
        }
    }

    public void cancelTime() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the timeId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual time selections.
     */
    private void setFragmentParameter(String timeId) {
        String fragmentParameter;
        if (timeId == null || timeId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = timeId;
        }

        UI.getCurrent().navigate(TimesView.class, fragmentParameter);
    }

    /**
     * Opens the time form and clears its fields to make it ready for
     * entering a new time if timeId is null, otherwise loads the time
     * with the given timeId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param timeId
     */
    public void enter(String timeId) {
        if (timeId != null && !timeId.isEmpty()) {
            if (timeId.equals("new")) {
                newTime();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(timeId);
                    final Time time = findTime(pid);
                    view.selectRow(time);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Time findTime(int timeId) {
        return DataService.get().getTimeById(timeId);
    }

    public void saveTime(Time time) {
        final boolean newTime = time.isNewTime();
        view.clearSelection();
        view.updateTime(time);
        setFragmentParameter("");
        view.showNotification(time.getTimeName() + (newTime ? " created" : " updated"));
    }

    public void deleteTime(Time time) {
        view.clearSelection();
        view.removeTime(time);
        setFragmentParameter("");
        view.showNotification(time.getTimeName() + " removed");
    }

    public void editTime(Time time) {
        if (time == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(time.getId() + "");
        }
        view.editTime(time);
    }

    public void newTime() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editTime(new Time());
    }

    public void rowSelected(Time time) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editTime(time);
        }
    }
}
