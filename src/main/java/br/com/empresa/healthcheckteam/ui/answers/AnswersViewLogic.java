package br.com.empresa.healthcheckteam.ui.answers;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Answer;
import com.vaadin.flow.component.UI;

import java.io.Serializable;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the answer editor form and the data source, including
 * fetching and saving answers.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class AnswersViewLogic implements Serializable {

    private final AnswersView view;

    public AnswersViewLogic(AnswersView simpleCrudView) {
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewAnswerEnabled(false);
        }
    }

    public void cancelAnswer() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the answerId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual answer selections.
     */
    private void setFragmentParameter(String answerId) {
        String fragmentParameter;
        if (answerId == null || answerId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = answerId;
        }

        UI.getCurrent().navigate(AnswersView.class, fragmentParameter);
    }

    /**
     * Opens the answer form and clears its fields to make it ready for
     * entering a new answer if answerId is null, otherwise loads the answer
     * with the given answerId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param answerId
     */
    public void enter(String answerId) {
        if (answerId != null && !answerId.isEmpty()) {
            if (answerId.equals("new")) {
                newAnswer();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(answerId);
                    final Answer answer = findAnswer(pid);
                    view.selectRow(answer);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Answer findAnswer(int answerId) {
        return DataService.get().getAnswerById(answerId);
    }

    public void saveAnswer(Answer answer) {
        final boolean newAnswer = answer.isNewAnswer();
        view.clearSelection();
        view.updateAnswer(answer);
        setFragmentParameter("");
        view.showNotification(answer.getAnswer() + (newAnswer ? " created" : " updated"));
    }

    public void deleteAnswer(Answer answer) {
        view.clearSelection();
        view.removeAnswer(answer);
        setFragmentParameter("");
        view.showNotification(answer.getAnswer() + " removed");
    }

    public void editAnswer(Answer answer) {
        if (answer == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(answer.getId() + "");
        }
        view.editAnswer(answer);
    }

    public void newAnswer() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editAnswer(new Answer());
    }

    public void rowSelected(Answer answer) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editAnswer(answer);
        }
    }
}
