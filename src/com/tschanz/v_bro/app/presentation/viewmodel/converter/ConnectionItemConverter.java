package com.tschanz.v_bro.app.presentation.viewmodel.converter;

import com.tschanz.v_bro.app.presentation.viewmodel.JdbcRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MockRepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.RepoConnectionItem;
import com.tschanz.v_bro.app.presentation.viewmodel.XmlRepoConnectionItem;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.*;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.JdbcConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.RepoConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.XmlConnectionResponse;


public class ConnectionItemConverter {
    public static ConnectionParametersRequest toRequest(RepoConnectionItem connection) {
        ConnectionParametersRequest connectionParameters;
        switch (connection.getRepoType()) {
            case JDBC:
                JdbcRepoConnectionItem jdbcConnection = (JdbcRepoConnectionItem) connection;
                connectionParameters = new JdbcConnectionParametersRequest(jdbcConnection.getUrl(), jdbcConnection.getUser(), jdbcConnection.getPassword());
                break;
            case XML:
                XmlRepoConnectionItem xmlRepoConnectionItem = (XmlRepoConnectionItem) connection;
                connectionParameters = new XMlConnectionParametersRequest(xmlRepoConnectionItem.getFilename());
                break;
            case MOCK:
            default:
                connectionParameters = new MockConnectionParametersRequest();
                break;
        }

        return connectionParameters;
    }


    public static RepoConnectionItem fromResponse(RepoConnectionResponse repoConnection) {
        if (repoConnection == null) {
            return null;
        }  else {
            switch (repoConnection.repoType) {
                case JDBC:
                    JdbcConnectionResponse jdbcConnection = (JdbcConnectionResponse) repoConnection;
                    return new JdbcRepoConnectionItem(jdbcConnection.url, jdbcConnection.user, jdbcConnection.password);
                case XML:
                    XmlConnectionResponse xmlConnection = (XmlConnectionResponse) repoConnection;
                    return new XmlRepoConnectionItem(xmlConnection.filename);
                case MOCK:
                default:
                    return new MockRepoConnectionItem();
            }
        }
    }
}
