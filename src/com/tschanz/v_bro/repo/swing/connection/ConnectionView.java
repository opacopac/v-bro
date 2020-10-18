package com.tschanz.v_bro.repo.swing.connection;

import com.tschanz.v_bro.repo.domain.model.RepoType;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;


public interface ConnectionView {
    void addShowConnectionDialogListener(ActionListener actionListener);

    void addQuickConnectionItemListener(ItemListener itemListener);

    void addConnectListener(ActionListener actionListener);

    void addDisconnectListener(ActionListener actionListener);

    void showConnectionDialog(boolean visible);

    RepoType getRepoType();

    String getUrl();

    String getUser();

    String getPassword();

    String getFilename();

    void setJdbcConnectionData(String url, String user, String password);

    void setXmlConnectionData(String filename);

    void setMockConnectionData();

    void setConnectedState(boolean isConnected);
}
