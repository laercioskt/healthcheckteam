package br.com.empresa.healthcheckteam.ui.teams;

import br.com.empresa.healthcheckteam.backend.data2.Team;
import br.com.empresa.healthcheckteam.backend.repository.TeamRepository;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Team} entities.
 * <p>
 * Used to simplify the code in {@link TeamsView} and
 * {@link TeamsViewLogic}.
 */
public class TeamsDataProvider extends ListDataProvider<Team> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";
    private TeamRepository teamRepository;

    public TeamsDataProvider(TeamRepository teamRepository) {
        super(teamRepository.findAll());
        this.teamRepository = teamRepository;
    }

    /**
     * Store given team to the backing data service.
     *
     * @param team the updated or new team
     */
    public void save(Team team) {
        final boolean newTeam = team.isNew();

        teamRepository.save(team);
        if (newTeam) {
            refreshAll();
        } else {
            refreshItem(team);
        }
    }

    /**
     * Delete given team from the backing data service.
     *
     * @param team the team to be deleted
     */
    public void delete(Team team) {
        teamRepository.delete(team);
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for team name, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(team -> passesFilter(team.getName(), this.filterText));
    }

    @Override
    public Long getId(Team team) {
        Objects.requireNonNull(team, "Cannot provide an id for a null team.");

        return team.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
