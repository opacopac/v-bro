package com.tschanz.v_bro.repo.swing.connection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.elements.swing.element_class_selection.ElementClassSelectionView;
import com.tschanz.v_bro.common.swing.statusbar.StatusBarView;
import com.tschanz.v_bro.elements.swing.model.ElementClassItem;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.mock.model.MockConnectionParameters;
import com.tschanz.v_bro.repo.swing.model.QuickConnectionItem;
import com.tschanz.v_bro.repo.usecase.close_connection.CloseConnectionUseCase;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionUseCase;
import com.tschanz.v_bro.repo.xml.model.XmlConnectionParameters;


import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.stream.Collectors;


public class ConnectionController {
    private final ConnectionView connectionView;
    private final ElementClassSelectionView elementClassSelectionView;
    private final StatusBarView statusBarView;
    private final OpenConnectionUseCase openConnectionUc;
    private final ReadElementClassesUseCase readElementClassesUc;
    private final CloseConnectionUseCase closeConnectionUc;
    private OpenConnectionResponse.RepoConnection currentConnection;


    public OpenConnectionResponse.RepoConnection getCurrentConnection() { return currentConnection; }


    public ConnectionController(
        ConnectionView connectionView,
        ElementClassSelectionView elementClassSelectionView,
        StatusBarView statusBarView,
        OpenConnectionUseCase openConnectionUc,
        ReadElementClassesUseCase readElementClassesUc,
        CloseConnectionUseCase closeConnectionUc
    ) {
        this.connectionView = connectionView;
        this.elementClassSelectionView = elementClassSelectionView;
        this.statusBarView = statusBarView;
        this.openConnectionUc = openConnectionUc;
        this.readElementClassesUc = readElementClassesUc;
        this.closeConnectionUc = closeConnectionUc;

        this.connectionView.setConnectedState(false);
        this.connectionView.addShowConnectionDialogListener(this::onOpenDialogClicked);
        this.connectionView.addQuickConnectionItemListener(this::onQuickConnectionSelected);
        this.connectionView.addConnectListener(this::onConnectClicked);
        this.connectionView.addDisconnectListener(this::onDisconnectClicked);
    }


    private void onOpenDialogClicked(ActionEvent e) {
        this.connectionView.showConnectionDialog(true);
    }


    private void onQuickConnectionSelected(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            QuickConnectionItem quickConnectionItem = (QuickConnectionItem) itemEvent.getItem();
            if (quickConnectionItem.getRepoType() == RepoType.JDBC) {
                this.connectionView.setJdbcConnectionData(
                    quickConnectionItem.getUrl(),
                    quickConnectionItem.getUser(),
                    quickConnectionItem.getPassword()
                );
            } else if (quickConnectionItem.getRepoType() == RepoType.XML) {
                this.connectionView.setXmlConnectionData(quickConnectionItem.getFilename());
            } else if (quickConnectionItem.getRepoType() == RepoType.MOCK) {
                this.connectionView.setMockConnectionData();
            }
        }
    }


    private void onConnectClicked(ActionEvent e) {
        try {
            this.connectionView.showConnectionDialog(false);

            this.openConnection();
            this.connectionView.setConnectedState(true);
            this.statusBarView.setStatusInfo("connection established");

            List<ElementClassItem> elementClassItems = this.readElementClassesUc.readElementClasses(this.currentConnection).elementTableNames
                .stream()
                .map(ElementClassItem::new)
                .collect(Collectors.toList());

            this.elementClassSelectionView.updateElementClassList(elementClassItems);
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }
    }


    private void openConnection() throws VBroAppException {
        ConnectionParameters connectionParameters;
        switch (this.connectionView.getRepoType()) {
            case JDBC:
                connectionParameters = new JdbcConnectionParameters(this.connectionView.getUrl(), this.connectionView.getUser(), this.connectionView.getPassword());
                break;
            case XML:
                connectionParameters = new XmlConnectionParameters(this.connectionView.getFilename());
                break;
            case MOCK:
            default:
                connectionParameters = new MockConnectionParameters(); // TODO: temporary
                break;
        }

        OpenConnectionResponse response = this.openConnectionUc.connect(connectionParameters);
        this.currentConnection = response.repoConnection;
    }


    private void onDisconnectClicked(ActionEvent e) {
        try {
            this.connectionView.showConnectionDialog(false);
            this.closeConnectionUc.disconnect();
            this.connectionView.setConnectedState(false);
            this.currentConnection = null;
            this.statusBarView.setStatusInfo("connection closed");
        } catch (VBroAppException exception) {
            this.statusBarView.setStatusError(exception.getMessage());
        }
    }
}
