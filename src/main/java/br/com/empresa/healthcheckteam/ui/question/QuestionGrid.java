package br.com.empresa.healthcheckteam.ui.question;

import br.com.empresa.healthcheckteam.backend.data2.Question;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

/**
 * Grid of questions, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class QuestionGrid extends Grid<Question> {

    public QuestionGrid() {

        setSizeFull();

        addColumn(Question::getDescription).setHeader("Question description").setFlexGrow(20).setSortable(true).setKey("description");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("description").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("description").setVisible(true);
        } else {
            getColumnByKey("description").setVisible(true);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        // fetch browser width
        UI.getCurrent().getInternals().setExtendedClientDetails(null);
        UI.getCurrent().getPage().retrieveExtendedClientDetails(e -> {
            setColumnVisibility(e.getBodyClientWidth());
        });
    }

    public Question getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Question question) {
        getDataCommunicator().refresh(question);
    }

}
