package br.com.empresa.healthcheckteam.ui.question;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.data.Question;
import br.com.empresa.healthcheckteam.backend.repository.QuestionRepository;
import com.vaadin.flow.component.UI;

import java.io.Serializable;
import java.util.Optional;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the question editor form and the data source, including
 * fetching and saving questions.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class QuestionViewLogic implements Serializable {

    private QuestionRepository questionRepository;
    private final QuestionView view;

    public QuestionViewLogic(QuestionRepository questionRepository, QuestionView simpleCrudView) {
        this.questionRepository = questionRepository;
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewQuestionEnabled(false);
        }
    }

    public void cancelQuestion() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the questionId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual question selections.
     */
    private void setFragmentParameter(String questionId) {
        String fragmentParameter;
        if (questionId == null || questionId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = questionId;
        }

        UI.getCurrent().navigate(QuestionView.class, fragmentParameter);
    }

    /**
     * Opens the question form and clears its fields to make it ready for
     * entering a new question if questionId is null, otherwise loads the question
     * with the given questionId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param questionId
     */
    public void enter(String questionId) {
        if (questionId != null && !questionId.isEmpty()) {
            if (questionId.equals("new")) {
                newQuestion();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final Long pid = Long.parseLong(questionId);
                    findQuestion(pid).ifPresent(view::selectRow);
                } catch (final NumberFormatException ignored) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Optional<Question> findQuestion(Long questionId) {
        return questionRepository.findById(questionId);
    }

    public void saveQuestion(Question question) {
        final boolean newQuestion = question.isNew();
        view.clearSelection();
        view.updateQuestion(question);
        setFragmentParameter("");
        view.showNotification(question.getDescription() + (newQuestion ? " created" : " updated"));
    }

    public void deleteQuestion(Question question) {
        view.clearSelection();
        view.removeQuestion(question);
        setFragmentParameter("");
        view.showNotification(question.getDescription() + " removed");
    }

    public void editQuestion(Question question) {
        if (question == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(question.getId() + "");
        }
        view.editQuestion(question);
    }

    public void newQuestion() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editQuestion(new Question());
    }

    public void rowSelected(Question question) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editQuestion(question);
        }
    }
}
