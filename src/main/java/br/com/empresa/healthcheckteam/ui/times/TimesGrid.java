package br.com.empresa.healthcheckteam.ui.times;

import br.com.empresa.healthcheckteam.backend.data.Time;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;

/**
 * Grid of times, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class TimesGrid extends Grid<Time> {

    public TimesGrid() {

        setSizeFull();

        addColumn(Time::getTimeName).setHeader("Time name").setFlexGrow(20).setSortable(true).setKey("timename");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("timename").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("timename").setVisible(true);
        } else {
            getColumnByKey("timename").setVisible(true);
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

    public Time getSelectedRow() {
        Notification.show("asdasd");
        return asSingleSelect().getValue();
    }

    public void refresh(Time time) {
        getDataCommunicator().refresh(time);
    }

}
