package br.com.empresa.healthcheckteam.ui.assessments;

import br.com.empresa.healthcheckteam.authentication.AccessControl;
import br.com.empresa.healthcheckteam.authentication.AccessControlFactory;
import br.com.empresa.healthcheckteam.backend.DataService;
import br.com.empresa.healthcheckteam.backend.data.Assessment;
import com.vaadin.flow.component.UI;

import java.io.Serializable;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the assessment editor form and the data source, including
 * fetching and saving assessments.
 * <p>
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class AssessmentsViewLogic implements Serializable {

    private final AssessmentsView view;

    public AssessmentsViewLogic(AssessmentsView simpleCrudView) {
        view = simpleCrudView;
    }

    /**
     * Does the initialization of the inventory view including disabling the
     * buttons if the user doesn't have access.
     */
    public void init() {
        if (!AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            view.setNewAssessmentEnabled(false);
        }
    }

    public void cancelAssessment() {
        setFragmentParameter("");
        view.clearSelection();
    }

    /**
     * Updates the fragment without causing InventoryViewLogic navigator to
     * change view. It actually appends the assessmentId as a parameter to the URL.
     * The parameter is set to keep the view state the same during e.g. a
     * refresh and to enable bookmarking of individual assessment selections.
     */
    private void setFragmentParameter(String assessmentId) {
        String fragmentParameter;
        if (assessmentId == null || assessmentId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = assessmentId;
        }

        UI.getCurrent().navigate(AssessmentsView.class, fragmentParameter);
    }

    /**
     * Opens the assessment form and clears its fields to make it ready for
     * entering a new assessment if assessmentId is null, otherwise loads the assessment
     * with the given assessmentId and shows its data in the form fields so the
     * user can edit them.
     *
     * @param assessmentId
     */
    public void enter(String assessmentId) {
        if (assessmentId != null && !assessmentId.isEmpty()) {
            if (assessmentId.equals("new")) {
                newAssessment();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    final int pid = Integer.parseInt(assessmentId);
                    final Assessment assessment = findAssessment(pid);
                    view.selectRow(assessment);
                } catch (final NumberFormatException e) {
                }
            }
        } else {
            view.showForm(false);
        }
    }

    private Assessment findAssessment(int assessmentId) {
        return DataService.get().getAssessmentById(assessmentId);
    }

    public void saveAssessment(Assessment assessment) {
        final boolean newAssessment = assessment.isNewAssessment();
        view.clearSelection();
        view.updateAssessment(assessment);
        setFragmentParameter("");
        view.showNotification(assessment.getAssessmentName() + (newAssessment ? " created" : " updated"));
    }

    public void deleteAssessment(Assessment assessment) {
        view.clearSelection();
        view.removeAssessment(assessment);
        setFragmentParameter("");
        view.showNotification(assessment.getAssessmentName() + " removed");
    }

    public void editAssessment(Assessment assessment) {
        if (assessment == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(assessment.getId() + "");
        }
        view.editAssessment(assessment);
    }

    public void newAssessment() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editAssessment(new Assessment());
    }

    public void rowSelected(Assessment assessment) {
        if (AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessControl.ADMIN_ROLE_NAME)) {
            editAssessment(assessment);
        }
    }
}
