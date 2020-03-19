package br.com.empresa.healthcheckteam.ui.answers;

import br.com.empresa.healthcheckteam.backend.data.Answer;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;

/**
 * Grid of answers, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class AnswersGrid extends Grid<Answer> {

    public AnswersGrid() {

        setSizeFull();

        addColumn(Answer::getAnswer).setHeader("Answer description").setFlexGrow(20).setSortable(true).setKey("answer");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("answer").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("answer").setVisible(true);
        } else {
            getColumnByKey("answer").setVisible(true);
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

    public Answer getSelectedRow() {
        Notification.show("asdasd");
        return asSingleSelect().getValue();
    }

    public void refresh(Answer answer) {
        getDataCommunicator().refresh(answer);
    }

}
