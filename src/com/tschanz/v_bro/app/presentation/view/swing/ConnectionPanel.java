package com.tschanz.v_bro.app.presentation.view.swing;

import com.tschanz.v_bro.app.presentation.viewmodel.actions.ConnectToRepoAction;
import com.tschanz.v_bro.common.reactive.BehaviorSubject;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.app.presentation.view.ConnectionView;
import com.tschanz.v_bro.app.presentation.viewmodel.JdbcRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.QuickConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.XmlRepoConnectionItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.Flow;


public class ConnectionPanel extends JPanel implements ConnectionView {
    private final ConnectionDialog connectionDialog = new ConnectionDialog();
    private final String NOT_CONNECTED_VALUE = "(not connected)";
    private final JLabel connectionString = new JLabel(this.NOT_CONNECTED_VALUE);


    public ConnectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        JButton connectionDialogButton = new JButton("Connection...");
        this.add(connectionDialogButton);
        this.add(new JLabel("Connection:"));
        this.add(this.connectionString);
        this.connectionDialog.setLocationRelativeTo(this);

        connectionDialogButton.addActionListener(this::onOpenDialogClicked);
    }


    @Override
    public void bindViewModel(
        Flow.Publisher<List<QuickConnectionItem>> quickConnectionList,
        ConnectToRepoAction connectToRepoAction,
        BehaviorSubject<RepoConnectionItem> currentRepoConnection
    ) {
        this.connectionDialog.bindQuickConnectionList(quickConnectionList);
        this.connectionDialog.bindConnectToRepoAction(connectToRepoAction);
        this.connectionDialog.bindCurrentRepoConnection(currentRepoConnection);
        currentRepoConnection.subscribe(new GenericSubscriber<>(this::onRepoConnectionChanged));
    }


    private void onOpenDialogClicked(ActionEvent e) {
        this.connectionDialog.setVisible(true);
    }


    private void onRepoConnectionChanged(RepoConnectionItem repoConnection) {
        this.connectionDialog.setVisible(false);
        this.connectionString.setText(this.createConnectionString(repoConnection));
    }


    private String createConnectionString(RepoConnectionItem repoConnection) {
        if (repoConnection == null) {
            return NOT_CONNECTED_VALUE;
        } else if (repoConnection.repoType == RepoType.JDBC) {
            JdbcRepoConnectionItem jdbcRepoConnection = (JdbcRepoConnectionItem) repoConnection;
            String user = jdbcRepoConnection.getUser();
            return jdbcRepoConnection.getUrl() + ((user == null || user.isEmpty()) ? "" : " (" + user + ")");
        } else if (repoConnection.repoType == RepoType.XML) {
            return ((XmlRepoConnectionItem) repoConnection).getFilename();
        } else {
            return "(Mock connection)";
        }
    }
}
