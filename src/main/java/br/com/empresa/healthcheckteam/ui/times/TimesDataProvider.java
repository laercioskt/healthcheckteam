package br.com.empresa.healthcheckteam.ui.times;

import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Time;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Time} entities.
 * <p>
 * Used to simplify the code in {@link TimesView} and
 * {@link TimesViewLogic}.
 */
public class TimesDataProvider extends ListDataProvider<Time> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";

    public TimesDataProvider() {
        super(DataService.get().getAllTimes());
    }

    /**
     * Store given time to the backing data service.
     *
     * @param time the updated or new time
     */
    public void save(Time time) {
        final boolean newTime = time.isNewTime();

        DataService.get().updateTime(time);
        if (newTime) {
            refreshAll();
        } else {
            refreshItem(time);
        }
    }

    /**
     * Delete given time from the backing data service.
     *
     * @param time the time to be deleted
     */
    public void delete(Time time) {
        DataService.get().deleteTime(time.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for time name, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(time -> passesFilter(time.getTimeName(), this.filterText));
    }

    @Override
    public Integer getId(Time time) {
        Objects.requireNonNull(time, "Cannot provide an id for a null time.");

        return time.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
