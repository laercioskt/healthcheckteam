package br.com.empresa.healthcheckteam.ui.questoes;

import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Questao;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

/**
 * Utility class that encapsulates filtering and CRUD operations for
 * {@link Questao} entities.
 * <p>
 * Used to simplify the code in {@link QuestoesView} and
 * {@link QuestoesViewLogic}.
 */
public class QuestoesDataProvider extends ListDataProvider<Questao> {

    /**
     * Text filter that can be changed separately.
     */
    private String filterText = "";

    public QuestoesDataProvider() {
        super(DataService.get().getAllQuestoes());
    }

    /**
     * Store given questao to the backing data service.
     *
     * @param questao the updated or new questao
     */
    public void save(Questao questao) {
        final boolean newQuestao = questao.isNewQuestao();

        DataService.get().updateQuestao(questao);
        if (newQuestao) {
            refreshAll();
        } else {
            refreshItem(questao);
        }
    }

    /**
     * Delete given questao from the backing data service.
     *
     * @param questao the questao to be deleted
     */
    public void delete(Questao questao) {
        DataService.get().deleteQuestao(questao.getId());
        refreshAll();
    }

    /**
     * Sets the filter to use for this data provider and refreshes data.
     * <p>
     * Filter is compared for questao name, availability and category.
     *
     * @param filterText the text to filter by, never null
     */
    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(questao -> passesFilter(questao.getDescricao(), this.filterText));
    }

    @Override
    public Integer getId(Questao questao) {
        Objects.requireNonNull(questao, "Cannot provide an id for a null questao.");

        return questao.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH).contains(filterText);
    }
}
