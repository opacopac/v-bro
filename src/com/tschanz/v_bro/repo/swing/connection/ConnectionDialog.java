package com.tschanz.v_bro.repo.swing.connection;

import com.tschanz.v_bro.repo.swing.model.QuickConnectionItem;

import javax.swing.*;
import java.awt.*;


public class ConnectionDialog extends JDialog {
    private final JPanel panel = new JPanel();
    private final JLabel connectionTypeLabel = new JLabel("Type");
    private final JPanel connectionTypePanel = new JPanel();
    private final JRadioButton jdbcConnectionTypeRadio = new JRadioButton("JDBC", true);
    private final JRadioButton xmlConnectionTypeRadio = new JRadioButton("XML", false);
    private final JRadioButton mockConnectionTypeRadio = new JRadioButton("Mock", false);
    private final JLabel quickConnectionsLabel = new JLabel("Quick Links");
    private final JComboBox<QuickConnectionItem> quickConnectionsList = new JComboBox<>();
    private final JLabel urlLabel = new JLabel("URL");
    private final JTextField urlField = new JTextField();
    private final JLabel userLabel = new JLabel("User");
    private final JTextField userField = new JTextField();
    private final JLabel passwordLabel = new JLabel("Password");
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel filenameLabel = new JLabel("XML File");
    private final JTextField filenameField = new JTextField();
    private final JButton connectButton = new JButton("connect");
    private final JButton disconnectButton = new JButton("disconnect");


    public JRadioButton getJdbcConnectionTypeRadio() { return jdbcConnectionTypeRadio; }
    public JRadioButton getXmlConnectionTypeRadio() { return xmlConnectionTypeRadio; }
    public JRadioButton getMockConnectionTypeRadio() { return mockConnectionTypeRadio; }
    public JComboBox<QuickConnectionItem> getQuickConnectionsList() { return quickConnectionsList; }
    public JTextField getUrlField() { return urlField; }
    public JTextField getUserField() { return userField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JTextField getFilenameField() { return filenameField; }
    public JButton getConnectButton() { return connectButton; }
    public JButton getDisconnectButton() { return disconnectButton; }


    public ConnectionDialog() {
        this.createLayout();
    }


    public void createLayout() {
        // connection type group
        this.connectionTypePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.connectionTypePanel.add(this.jdbcConnectionTypeRadio);
        this.connectionTypePanel.add(this.xmlConnectionTypeRadio);
        this.connectionTypePanel.add(this.mockConnectionTypeRadio);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.jdbcConnectionTypeRadio);
        buttonGroup.add(this.xmlConnectionTypeRadio);
        buttonGroup.add(this.mockConnectionTypeRadio);

        // main dialog
        this.setSize(500, 270);
        this.add(this.panel);
        GroupLayout groupLayout = new GroupLayout(this.panel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.quickConnectionsLabel)
                    .addComponent(this.connectionTypeLabel)
                    .addComponent(this.urlLabel)
                    .addComponent(this.userLabel)
                    .addComponent(this.passwordLabel)
                    .addComponent(this.filenameLabel)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.quickConnectionsList)
                    .addComponent(this.connectionTypePanel)
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
                    .addComponent(this.quickConnectionsLabel)
                    .addComponent(this.quickConnectionsList)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.connectionTypeLabel)
                    .addComponent(this.connectionTypePanel)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.urlLabel)
                    .addComponent(this.urlField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.userLabel)
                    .addComponent(this.userField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.passwordLabel)
                    .addComponent(this.passwordField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.filenameLabel)
                    .addComponent(this.filenameField)
                )
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.connectButton)
                    .addComponent(this.disconnectButton)
                )
        );
        this.panel.setLayout(groupLayout);

        // TODO: read from properties file
        this.quickConnectionsList.addItem(QuickConnectionItem.createEmptyItem("(select)"));

        this.quickConnectionsList.addItem(QuickConnectionItem.createJdbcItem("local mysql", "jdbc:mysql://localhost:3306/test", "", ""));
        this.quickConnectionsList.addItem(QuickConnectionItem.createJdbcItem("NOVAP_BETA", "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=porasbbselscan.sbb.ch)(PORT=1551))(CONNECT_DATA=(SERVICE_NAME=ZPS_ENTW)(CatalogOptions=0)))", "NOVAP_BETA", "NOVAP_BETA"));
        this.quickConnectionsList.addItem(QuickConnectionItem.createJdbcItem("NOVAP_BETA (slow)", "jdbc:oracle:thin:@porasbbselscan.sbb.ch:1551/ZPS_ENTW", "NOVAP_BETA", "NOVAP_BETA"));

        this.quickConnectionsList.addItem(QuickConnectionItem.createXmlItem("mini_dr.xml", "./res/mini_dr.xml"));
        this.quickConnectionsList.addItem(QuickConnectionItem.createXmlItem("stammdaten.xml", "./res/stammdaten.xml"));

        this.quickConnectionsList.addItem(QuickConnectionItem.createMockItem("Mock"));
    }
}
