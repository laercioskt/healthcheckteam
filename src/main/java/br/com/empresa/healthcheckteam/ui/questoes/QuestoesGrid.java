package br.com.empresa.healthcheckteam.ui.questoes;

import br.com.empresa.healthcheckteam.backend.data.Questao;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;

/**
 * Grid of questoes, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class QuestoesGrid extends Grid<Questao> {

    public QuestoesGrid() {

        setSizeFull();

        addColumn(Questao::getDescricao).setHeader("Question name").setFlexGrow(20).setSortable(true).setKey("descricao");

        // If the browser window size changes, check if all columns fit on
        // screen
        // (e.g. switching from portrait to landscape mode)
        UI.getCurrent().getPage().addBrowserWindowResizeListener(e -> setColumnVisibility(e.getWidth()));
    }

    private void setColumnVisibility(int width) {
        if (width > 800) {
            getColumnByKey("descricao").setVisible(true);
        } else if (width > 550) {
            getColumnByKey("descricao").setVisible(true);
        } else {
            getColumnByKey("descricao").setVisible(true);
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

    public Questao getSelectedRow() {
        Notification.show("asdasd");
        return asSingleSelect().getValue();
    }

    public void refresh(Questao questao) {
        getDataCommunicator().refresh(questao);
    }

}
