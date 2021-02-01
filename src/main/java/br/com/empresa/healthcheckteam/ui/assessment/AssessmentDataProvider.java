package br.com.empresa.healthcheckteam.ui.assessment;

import br.com.empresa.healthcheckteam.backend.data2.Assessment;
import br.com.empresa.healthcheckteam.backend.repository.AssessmentRepository;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Assessment} entities.
 * <p>
 * Used to simplify the code in {@link AssessmentView} and
 * {@link AssessmentViewLogic}.
 */
public class AssessmentDataProvider extends ListDataProvider<Assessment> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";
    private final AssessmentRepository assessmentRepository;

    public AssessmentDataProvider(AssessmentRepository assessmentRepository) {
        super(assessmentRepository.findAll());
        this.assessmentRepository = assessmentRepository;
    }

    /**
     * Store given assessment to the backing data service.
     *
     * @param assessment the updated or new assessment
     */
    public void save(Assessment assessment) {
        final boolean newAssessment = assessment.isNew();

        assessmentRepository.save(assessment);
        if (newAssessment) {
            getItems().clear();
            getItems().addAll(assessmentRepository.findAll());
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
        assessmentRepository.delete(assessment);
        getItems().clear();
        getItems().addAll(assessmentRepository.findAll());
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

        setFilter(assessment -> passesFilter(assessment.getDescription(), this.filterText));
    }

    @Override
    public Long getId(Assessment assessment) {
        Objects.requireNonNull(assessment, "Cannot provide an id for a null assessment.");

        return assessment.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
