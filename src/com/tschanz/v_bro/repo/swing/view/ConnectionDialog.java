package com.tschanz.v_bro.repo.swing.view;

import com.tschanz.v_bro.common.BehaviorSubject;
import com.tschanz.v_bro.common.GenericSubscriber;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.swing.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;


public class ConnectionDialog extends JDialog {
    private final JRadioButton jdbcConnectionTypeRadio = new JRadioButton("JDBC", true);
    private final JRadioButton xmlConnectionTypeRadio = new JRadioButton("XML", false);
    private final JRadioButton mockConnectionTypeRadio = new JRadioButton("Mock", false);
    private final JComboBox<QuickConnectionItem> quickConnectionsList = new JComboBox<>();
    private final JTextField urlField = new JTextField();
    private final JTextField userField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField filenameField = new JTextField();
    private final JButton connectButton = new JButton("connect");
    private final JButton disconnectButton = new JButton("disconnect");
    private BehaviorSubject<RepoConnectionItem> connectToRepoAction;


    public ConnectionDialog() {
        this.createLayout();

        this.quickConnectionsList.addItemListener(this::onQuickConnectionSelected);
        this.jdbcConnectionTypeRadio.addActionListener(this::onJdbcConnectionTypeRadioSelected);
        this.xmlConnectionTypeRadio.addActionListener(this::onXmlConnectionTypeRadioSelected);
        this.mockConnectionTypeRadio.addActionListener(this::onMockConnectionTypeRadioSelected);
        this.connectButton.addActionListener(this::onConnectClicked);
        this.disconnectButton.addActionListener(this::onDisconnectClicked);
    }


    public void bindQuickConnectionList(BehaviorSubject<List<QuickConnectionItem>> quickConnectionList) {
        quickConnectionList.subscribe(new GenericSubscriber<>(this::onQuickConnectionListChanged));
    }


    public void bindConnectToRepoAction(BehaviorSubject<RepoConnectionItem> connectToRepoAction) {
        this.connectToRepoAction = connectToRepoAction;
    }


    public void bindCurrentRepoConnection(BehaviorSubject<RepoConnectionItem> currentRepoConnection) {
        currentRepoConnection.subscribe(new GenericSubscriber<>(this::onRepoConnectionChanged));
    }


    private void createLayout() {
        // repo_connection type group
        JPanel connectionTypePanel = new JPanel();
        connectionTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        connectionTypePanel.add(this.jdbcConnectionTypeRadio);
        connectionTypePanel.add(this.xmlConnectionTypeRadio);
        connectionTypePanel.add(this.mockConnectionTypeRadio);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.jdbcConnectionTypeRadio);
        buttonGroup.add(this.xmlConnectionTypeRadio);
        buttonGroup.add(this.mockConnectionTypeRadio);

        // main dialog
        this.setSize(500, 270);
        JPanel panel = new JPanel();
        this.add(panel);
        GroupLayout groupLayout = new GroupLayout(panel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        JLabel quickConnectionsLabel = new JLabel("Quick Links");
        JLabel connectionTypeLabel = new JLabel("Type");
        JLabel urlLabel = new JLabel("URL");
        JLabel userLabel = new JLabel("User");
        JLabel passwordLabel = new JLabel("Password");
        JLabel filenameLabel = new JLabel("XML File");
        groupLayout.setHorizontalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(quickConnectionsLabel)
                    .addComponent(connectionTypeLabel)
                    .addComponent(urlLabel)
                    .addComponent(userLabel)
                    .addComponent(passwordLabel)
                    .addComponent(filenameLabel)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.quickConnectionsList)
                    .addComponent(connectionTypePanel)
                    .addComponent(this.urlField)
                    .addComponent(this.userField)
                    .addComponent(this.passwordField)
                    .addComponent(this.filenameField)
                    .addComponent(this.connectButton)
                    .addComponent(this.disconnectButton)
                )
        );
        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(quickConnectionsLabel)
                    .addComponent(this.quickConnectionsList)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(connectionTypeLabel)
                    .addComponent(connectionTypePanel)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(urlLabel)
                    .addComponent(this.urlField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(this.userField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(this.passwordField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(filenameLabel)
                    .addComponent(this.filenameField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.connectButton)
                    .addComponent(this.disconnectButton)
                )
        );
        panel.setLayout(groupLayout);
    }


    private void onQuickConnectionListChanged(List<QuickConnectionItem> quickConnectionList) {
        this.quickConnectionsList.removeAllItems();
        this.quickConnectionsList.addItem(QuickConnectionItem.createEmptyItem("(select)"));

        for (QuickConnectionItem item : quickConnectionList) {
            this.quickConnectionsList.addItem(
                new QuickConnectionItem(
                    item.getKey(),
                    item.getRepoType(),
                    item.getUrl(),
                    item.getUser(),
                    item.getPassword(),
                    item.getFilename()
                )
            );
        }
    }


    private void onQuickConnectionSelected(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            QuickConnectionItem quickConnectionItem = (QuickConnectionItem) itemEvent.getItem();
            this.selectRepoType(quickConnectionItem.getRepoType());
            this.enableDisableFields(quickConnectionItem.getRepoType());
            this.urlField.setText(quickConnectionItem.getUrl());
            this.userField.setText(quickConnectionItem.getUser());
            this.passwordField.setText(quickConnectionItem.getPassword());
            this.filenameField.setText(quickConnectionItem.getFilename());
        }
    }


    private void onJdbcConnectionTypeRadioSelected(ActionEvent actionEvent) {
        this.enableDisableFields(RepoType.JDBC);
    }


    private void onXmlConnectionTypeRadioSelected(ActionEvent actionEvent) {
        this.enableDisableFields(RepoType.XML);
    }


    private void onMockConnectionTypeRadioSelected(ActionEvent actionEvent) {
        this.enableDisableFields(RepoType.MOCK);
    }


    private void onConnectClicked(ActionEvent e) {
        RepoType repoType = this.getSelectedRepoType();
        RepoConnectionItem nextRepoConnection;
        switch (repoType) {
            case JDBC:
                nextRepoConnection = new JdbcRepoConnectionItem(this.urlField.getText(), this.userField.getText(), String.valueOf(this.passwordField.getPassword()));
                break;
            case XML:
                nextRepoConnection = new XmlRepoConnectionItem(this.filenameField.getText());
                break;
            case MOCK:
            default:
                nextRepoConnection = new MockRepoConnectionItem();
                break;
        }

        this.connectToRepoAction.next(nextRepoConnection);
    }


    private void onDisconnectClicked(ActionEvent e) {
        this.connectToRepoAction.next(null);
    }



    private void onRepoConnectionChanged(RepoConnectionItem repoConnection) {
        if (repoConnection != null) {
            this.connectButton.setVisible(false);
            this.disconnectButton.setVisible(true);
        } else {
            this.connectButton.setVisible(true);
            this.disconnectButton.setVisible(false);
        }
    }


    private RepoType getSelectedRepoType() {
        if (this.jdbcConnectionTypeRadio.isSelected()) {
            return RepoType.JDBC;
        } else if (this.xmlConnectionTypeRadio.isSelected()) {
            return RepoType.XML;
        } else if (this.mockConnectionTypeRadio.isSelected()) {
            return RepoType.MOCK;
        } else {
            return RepoType.MOCK;
        }
    }


    private void selectRepoType(RepoType repoType) {
        if (repoType == RepoType.JDBC) {
            this.jdbcConnectionTypeRadio.setSelected(true);
            this.xmlConnectionTypeRadio.setSelected(false);
            this.mockConnectionTypeRadio.setSelected(false);
        } else if (repoType == RepoType.XML) {
            this.jdbcConnectionTypeRadio.setSelected(false);
            this.xmlConnectionTypeRadio.setSelected(true);
            this.mockConnectionTypeRadio.setSelected(false);
        } else {
            this.jdbcConnectionTypeRadio.setSelected(false);
            this.xmlConnectionTypeRadio.setSelected(false);
            this.mockConnectionTypeRadio.setSelected(true);
        }
    }


    private void enableDisableFields(RepoType repoType) {
        if (repoType == RepoType.JDBC) {
            this.urlField.setEnabled(true);
            this.userField.setEnabled(true);
            this.passwordField.setEnabled(true);
            this.filenameField.setEnabled(false);
        } else if (repoType == RepoType.XML) {
            this.urlField.setEnabled(false);
            this.userField.setEnabled(false);
            this.passwordField.setEnabled(false);
            this.filenameField.setEnabled(true);
        } else {
            this.urlField.setEnabled(false);
            this.userField.setEnabled(false);
            this.passwordField.setEnabled(false);
            this.filenameField.setEnabled(false);
        }
    }
}
