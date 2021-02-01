package br.com.empresa.healthcheckteam.ui.members;

import br.com.empresa.healthcheckteam.backend.data.Member;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;

/**
 * Grid of members, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class MembersGrid extends Grid<Member> {

    public MembersGrid() {

        setSizeFull();

        addColumn(Member::getName).setHeader("Member name").setFlexGrow(20).setSortable(true).setKey("name");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("name").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("name").setVisible(true);
        } else {
            getColumnByKey("name").setVisible(true);
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

    public Member getSelectedRow() {
        return asSingleSelect().getValue();
    }

    public void refresh(Member member) {
        getDataCommunicator().refresh(member);
    }

}
