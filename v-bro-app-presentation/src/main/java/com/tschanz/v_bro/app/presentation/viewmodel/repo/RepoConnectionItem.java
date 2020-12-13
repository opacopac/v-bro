package com.tschanz.v_bro.app.presentation.viewmodel.repo;

import com.tschanz.v_bro.app.presenter.repo.JdbcConnectionResponse;
import com.tschanz.v_bro.app.presenter.repo.RepoResponse;
import com.tschanz.v_bro.app.presenter.repo.XmlConnectionResponse;
import com.tschanz.v_bro.app.usecase.open_repo.*;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public abstract class RepoConnectionItem {
    @NonNull public final RepoType repoType;


    public static OpenRepoRequest toRequest(RepoConnectionItem connection) {
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

        return new OpenRepoRequest(connectionParameters);
    }


    public static RepoConnectionItem fromResponse(RepoResponse response) {
        if (response.getConnection() == null) {
            return null;
        }  else {
            switch (response.getConnection().repoType) {
                case JDBC:
                    JdbcConnectionResponse jdbcConnection = (JdbcConnectionResponse) response.getConnection();
                    return new JdbcRepoConnectionItem(jdbcConnection.url, jdbcConnection.user, jdbcConnection.password);
                case XML:
                    XmlConnectionResponse xmlConnection = (XmlConnectionResponse) response.getConnection();
                    return new XmlRepoConnectionItem(xmlConnection.filename);
                case MOCK:
                default:
                    return new MockRepoConnectionItem();
            }
        }
    }
}
