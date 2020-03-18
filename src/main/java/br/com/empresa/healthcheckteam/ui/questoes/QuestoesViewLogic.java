package br.com.empresa.healthcheckteam.ui.questoes;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Questao;
import com.vaadin.flow.component.UI;

import java.io.Serializable;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the questao editor form and the data source, including
 * fetching and saving questoes.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class QuestoesViewLogic implements Serializable {

    private final QuestoesView view;

    public QuestoesViewLogic(QuestoesView simpleCrudView) {
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewQuestaoEnabled(false);
        }
    }

    public void cancelQuestao() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the questaoId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual questao selections.
     */
    private void setFragmentParameter(String questaoId) {
        String fragmentParameter;
        if (questaoId == null || questaoId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = questaoId;
        }

        UI.getCurrent().navigate(QuestoesView.class, fragmentParameter);
    }

    /**
     * Opens the questao form and clears its fields to make it ready for
     * entering a new questao if questaoId is null, otherwise loads the questao
     * with the given questaoId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param questaoId
     */
    public void enter(String questaoId) {
        if (questaoId != null && !questaoId.isEmpty()) {
            if (questaoId.equals("new")) {
                newQuestao();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(questaoId);
                    final Questao questao = findQuestao(pid);
                    view.selectRow(questao);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Questao findQuestao(int questaoId) {
        return DataService.get().getQuestaoById(questaoId);
    }

    public void saveQuestao(Questao questao) {
        final boolean newQuestao = questao.isNewQuestao();
        view.clearSelection();
        view.updateQuestao(questao);
        setFragmentParameter("");
        view.showNotification(questao.getDescricao() + (newQuestao ? " created" : " updated"));
    }

    public void deleteQuestao(Questao questao) {
        view.clearSelection();
        view.removeQuestao(questao);
        setFragmentParameter("");
        view.showNotification(questao.getDescricao() + " removed");
    }

    public void editQuestao(Questao questao) {
        if (questao == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(questao.getId() + "");
        }
        view.editQuestao(questao);
    }

    public void newQuestao() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editQuestao(new Questao());
    }

    public void rowSelected(Questao questao) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editQuestao(questao);
        }
    }
}
