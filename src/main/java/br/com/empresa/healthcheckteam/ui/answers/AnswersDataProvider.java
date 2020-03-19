package br.com.empresa.healthcheckteam.ui.answers;

import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Answer;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Answer} entities.
 * <p>
 * Used to simplify the code in {@link AnswersView} and
 * {@link AnswersViewLogic}.
 */
public class AnswersDataProvider extends ListDataProvider<Answer> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";

    public AnswersDataProvider() {
        super(DataService.get().getAllAnswers());
    }

    /**
     * Store given answer to the backing data service.
     *
     * @param answer the updated or new answer
     */
    public void save(Answer answer) {
        final boolean newAnswer = answer.isNewAnswer();

        DataService.get().updateAnswer(answer);
        if (newAnswer) {
            refreshAll();
        } else {
            refreshItem(answer);
        }
    }

    /**
     * Delete given answer from the backing data service.
     *
     * @param answer the answer to be deleted
     */
    public void delete(Answer answer) {
        DataService.get().deleteAnswer(answer.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for answer description, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(answer -> passesFilter(answer.getAnswer(), this.filterText));
    }

    @Override
    public Integer getId(Answer answer) {
        Objects.requireNonNull(answer, "Cannot provide an id for a null answer.");

        return answer.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
