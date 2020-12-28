package com.tschanz.v_bro.app.usecase.open_repo;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.persistence.mock.model.MockConnectionParameters;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class ConnectionParametersRequest {
    public final RepoType repoType;


    public static ConnectionParameters fromRequest(ConnectionParametersRequest requestConnectionParameters) {
        ConnectionParameters connectionParameters;
        switch (requestConnectionParameters.repoType) {
            case JDBC:
                var jdbcConnectionParameters = (JdbcConnectionParametersRequest) requestConnectionParameters;
                connectionParameters = new JdbcConnectionParameters(jdbcConnectionParameters.url, jdbcConnectionParameters.user, jdbcConnectionParameters.password);
                break;
            case XML:
                var xmlRepoConnectionParameters = (XMlConnectionParametersRequest) requestConnectionParameters;
                connectionParameters =  new XmlConnectionParameters(xmlRepoConnectionParameters.filename);
                break;
            case MOCK:
            default:
                connectionParameters =  new MockConnectionParameters();
                break;
        }

        return connectionParameters;
    }
}
