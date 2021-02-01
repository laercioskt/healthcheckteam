package br.com.empresa.healthcheckteam.ui.teams;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.data2.Team;
import br.com.empresa.healthcheckteam.backend.repository.TeamRepository;
import com.vaadin.flow.component.UI;

import java.io.Serializable;
import java.util.Optional;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the team editor form and the data source, including
 * fetching and saving teams.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class TeamsViewLogic implements Serializable {

    private TeamRepository teamRepository;
    private final TeamsView view;

    public TeamsViewLogic(TeamRepository teamRepository, TeamsView simpleCrudView) {
        this.teamRepository = teamRepository;
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewTeamEnabled(false);
        }
    }

    public void cancelTeam() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the teamId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual team selections.
     */
    private void setFragmentParameter(String teamId) {
        String fragmentParameter;
        if (teamId == null || teamId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = teamId;
        }

        UI.getCurrent().navigate(TeamsView.class, fragmentParameter);
    }

    /**
     * Opens the team form and clears its fields to make it ready for
     * entering a new team if teamId is null, otherwise loads the team
     * with the given teamId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param teamId
     */
    public void enter(String teamId) {
        if (teamId != null && !teamId.isEmpty()) {
            if (teamId.equals("new")) {
                newTeam();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final Long pid = Long.parseLong(teamId);
                    final Team team = findTeam(pid);
                    view.selectRow(team);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Team findTeam(Long teamId) {
        Optional<Team> maybeATeam = teamRepository.findById(teamId);
        assert maybeATeam.isPresent();
        return maybeATeam.get();
    }

    public void saveTeam(Team team) {
        final boolean newTeam = team.isNew();
        view.clearSelection();
        view.updateTeam(team);
        setFragmentParameter("");
        view.showNotification(team.getName() + (newTeam ? " created" : " updated"));
    }

    public void deleteTeam(Team team) {
        view.clearSelection();
        view.removeTeam(team);
        setFragmentParameter("");
        view.showNotification(team.getName() + " removed");
    }

    public void editTeam(Team team) {
        if (team == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(team.getId() + "");
        }
        view.editTeam(team);
    }

    public void newTeam() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editTeam(new Team());
    }

    public void rowSelected(Team team) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editTeam(team);
        }
    }
}
