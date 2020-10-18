package com.tschanz.v_bro.repo.swing.connection;

import com.tschanz.v_bro.repo.domain.model.RepoType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;


public class ConnectionPanel extends JPanel implements ConnectionView {
    private final ConnectionDialog connectionDialog = new ConnectionDialog();
    private final String NOT_CONNECTED_VALUE = "(not connected)";
    private final JButton connectionDialogButton = new JButton("Connection...");
    private final JLabel connectionString = new JLabel(this.NOT_CONNECTED_VALUE);


    public ConnectionPanel() {
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(this.connectionDialogButton);
        this.add(new JLabel("Connection:"));
        this.add(this.connectionString);

        this.connectionDialog.setLocationRelativeTo(this);
    }


    @Override
    public void showConnectionDialog(boolean visible) {
        this.connectionDialog.setVisible(visible);
    }


    @Override
    public RepoType getRepoType() {
        if (this.connectionDialog.getJdbcConnectionTypeRadio().isSelected()) {
            return RepoType.JDBC;
        } else if (this.connectionDialog.getXmlConnectionTypeRadio().isSelected()) {
            return RepoType.XML;
        } else if (this.connectionDialog.getMockConnectionTypeRadio().isSelected()) {
            return RepoType.MOCK;
        } else {
            return null;
        }
    }


    @Override
    public void addShowConnectionDialogListener(ActionListener actionListener) {
        this.connectionDialogButton.addActionListener(actionListener);
    }


    @Override
    public void addQuickConnectionItemListener(ItemListener itemListener) {
        this.connectionDialog.getQuickConnectionsList().addItemListener(itemListener);
    }


    @Override
    public void addConnectListener(ActionListener actionListener) {
        this.connectionDialog.getConnectButton().addActionListener(actionListener);
    }


    @Override
    public void addDisconnectListener(ActionListener actionListener) {
        this.connectionDialog.getDisconnectButton().addActionListener(actionListener);
    }


    @Override
    public String getUrl() {
        return this.connectionDialog.getUrlField().getText();
    }


    @Override
    public String getUser() {
        return this.connectionDialog.getUserField().getText();
    }


    @Override
    public String getPassword() {
        return new String(this.connectionDialog.getPasswordField().getPassword());
    }


    @Override
    public String getFilename() {
        return this.connectionDialog.getFilenameField().getText();
    }


    @Override
    public void setJdbcConnectionData(String url, String user, String password) {
        this.connectionDialog.getJdbcConnectionTypeRadio().setSelected(true);
        this.connectionDialog.getXmlConnectionTypeRadio().setSelected(false);
        this.connectionDialog.getMockConnectionTypeRadio().setSelected(false);
        this.connectionDialog.getUrlField().setText(url);
        this.connectionDialog.getUserField().setText(user);
        this.connectionDialog.getPasswordField().setText(password);
        this.connectionDialog.getFilenameField().setText("");
    }


    @Override
    public void setXmlConnectionData(String filename) {
        this.connectionDialog.getJdbcConnectionTypeRadio().setSelected(false);
        this.connectionDialog.getXmlConnectionTypeRadio().setSelected(true);
        this.connectionDialog.getMockConnectionTypeRadio().setSelected(false);
        this.connectionDialog.getUrlField().setText("");
        this.connectionDialog.getUserField().setText("");
        this.connectionDialog.getPasswordField().setText("");
        this.connectionDialog.getFilenameField().setText(filename);
    }


    @Override
    public void setMockConnectionData() {
        this.connectionDialog.getJdbcConnectionTypeRadio().setSelected(false);
        this.connectionDialog.getXmlConnectionTypeRadio().setSelected(false);
        this.connectionDialog.getMockConnectionTypeRadio().setSelected(true);
        this.connectionDialog.getUrlField().setText("");
        this.connectionDialog.getUserField().setText("");
        this.connectionDialog.getPasswordField().setText("");
        this.connectionDialog.getFilenameField().setText("");
    }


    @Override
    public void setConnectedState(boolean isConnected) {
        if (isConnected) {
            String user = this.getUser();
            String url = this.getUrl();
            this.connectionString.setText(user.length() > 0 ? url + " (" + user + ")" : url);
            this.connectionDialog.getConnectButton().setVisible(false);
            this.connectionDialog.getDisconnectButton().setVisible(true);
        } else {
            this.connectionString.setText(this.NOT_CONNECTED_VALUE);
            this.connectionDialog.getConnectButton().setVisible(true);
            this.connectionDialog.getDisconnectButton().setVisible(false);
        }
    }
}
