package br.com.empresa.healthcheckteam.ui.assessment;

import br.com.empresa.healthcheckteam.backend.data.Assessment;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Grid of assessments, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class AssessmentGrid extends Grid<Assessment> {

    public AssessmentGrid() {

        setSizeFull();

        addColumn(assessment -> assessment.getCreated().format(ofPattern("dd/MM/yyyy")))
                .setHeader("Assessment created").setFlexGrow(20).setSortable(true).setKey("created");
        addColumn(assessment -> assessment.getTeam().getName())
                .setHeader("Assessment team").setFlexGrow(20).setSortable(true).setKey("team");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("created").setVisible(true);
            getColumnByKey("team").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("created").setVisible(true);
            getColumnByKey("team").setVisible(true);
        } else {
            getColumnByKey("created").setVisible(true);
            getColumnByKey("team").setVisible(true);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e ->
                setColumnVisibility(e.getBodyClientWidth()));
    }

    public Assessment getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Assessment assessment) {
        getDataCommunicator().refresh(assessment);
    }

}
