package com.tschanz.v_bro.repo.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.swing.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


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
    public void bindQuickConnectionList(BehaviorSubject<List<QuickConnectionItem>> quickConnectionList) {
        this.connectionDialog.bindQuickConnectionList(quickConnectionList);
    }


    @Override
    public void bindConnectToRepoAction(BehaviorSubject<RepoConnectionItem> selectedRepoConnection) {
        this.connectionDialog.bindConnectToRepoAction(selectedRepoConnection);
    }


    @Override
    public void bindCurrentRepoConnection(BehaviorSubject<RepoConnectionItem> currentRepoConnection) {
        currentRepoConnection.subscribe(new GenericSubscriber<>(this::onRepoConnectionChanged));
        this.connectionDialog.bindCurrentRepoConnection(currentRepoConnection);
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
