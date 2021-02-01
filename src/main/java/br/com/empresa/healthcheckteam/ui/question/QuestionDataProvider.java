package br.com.empresa.healthcheckteam.ui.question;

import br.com.empresa.healthcheckteam.backend.data2.Question;
import br.com.empresa.healthcheckteam.backend.repository.QuestionRepository;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Question} entities.
 * <p>
 * Used to simplify the code in {@link QuestionView} and
 * {@link QuestionViewLogic}.
 */
public class QuestionDataProvider extends ListDataProvider<Question> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";
    private final QuestionRepository questionRepository;

    public QuestionDataProvider(QuestionRepository questionRepository) {
        super(questionRepository.findAll());
        this.questionRepository = questionRepository;
    }

    /**
     * Store given question to the backing data service.
     *
     * @param question the updated or new question
     */
    public void save(Question question) {
        final boolean newQuestion = question.isNew();

        questionRepository.save(question);
        if (newQuestion) {
            getItems().clear();
            getItems().addAll(questionRepository.findAll());
            refreshAll();
        } else {
            refreshItem(question);
        }
    }

    /**
     * Delete given question from the backing data service.
     *
     * @param question the question to be deleted
     */
    public void delete(Question question) {
        questionRepository.delete(question);
        getItems().clear();
        getItems().addAll(questionRepository.findAll());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for question name, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(question -> passesFilter(question.getDescription(), this.filterText));
    }

    @Override
    public Long getId(Question question) {
        Objects.requireNonNull(question, "Cannot provide an id for a null question.");

        return question.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
