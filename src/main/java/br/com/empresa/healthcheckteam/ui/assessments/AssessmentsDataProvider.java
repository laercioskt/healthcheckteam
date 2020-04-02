package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Assessment;
import br.com.empresa.healthcheckteam.backend.data.Time;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Time} entities.
 * <p>
 * Used to simplify the code in {@link AssessmentsView} and
 * {@link AssessmentsViewLogic}.
 */
public class AssessmentsDataProvider extends ListDataProvider<Assessment> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";

    public AssessmentsDataProvider() {
        super(DataService.get().getAllAssessments());
    }

    /**
     * Store given assessment to the backing data service.
     *
     * @param assessment the updated or new assessment
     */
    public void save(Assessment assessment) {
        final boolean newAssessment = assessment.isNewAssessment();

        DataService.get().updateAssessment(assessment);
        if (newAssessment) {
            refreshAll();
        } else {
            refreshItem(assessment);
        }
    }

    /**
     * Delete given assessment from the backing data service.
     *
     * @param assessment the assessment to be deleted
     */
    public void delete(Assessment assessment) {
        DataService.get().deleteAssessment(assessment.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for assessment name, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(assessment -> passesFilter(assessment.getAssessmentName(), this.filterText));
    }

    @Override
    public Integer getId(Assessment assessment) {
        Objects.requireNonNull(assessment, "Cannot provide an id for a null assessment.");

        return assessment.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
