package com.tschanz.v_bro.app.usecase.common.converter;

import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.ConnectionParametersRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.JdbcConnectionParametersRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.XMlConnectionParametersRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.JdbcConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.MockConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.RepoConnectionResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.XmlConnectionResponse;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.persistence.mock.model.MockConnectionParameters;
import com.tschanz.v_bro.repo.persistence.model.XmlConnectionParameters;


public class RepoConnectionConverter {
    public static RepoConnectionResponse toResponse(ConnectionParameters connectionParameters) {
        switch (connectionParameters.getRepoType()) {
            case JDBC:
                JdbcConnectionParameters jdbcConnectionParameters = (JdbcConnectionParameters) connectionParameters;
                return new JdbcConnectionResponse(jdbcConnectionParameters.getUrl(), jdbcConnectionParameters.getUser(), jdbcConnectionParameters.getPassword());
            case XML:
                XmlConnectionParameters xmlConnectionParameters = (XmlConnectionParameters) connectionParameters;
                return new XmlConnectionResponse(xmlConnectionParameters.getFilename());
            case MOCK:
            default:
                return new MockConnectionResponse();
        }
    }


    public static ConnectionParameters fromRequest(ConnectionParametersRequest requestConnectionParameters) {
        ConnectionParameters connectionParameters;
        switch (requestConnectionParameters.repoType) {
            case JDBC:
                JdbcConnectionParametersRequest jdbcConnectionParameters = (JdbcConnectionParametersRequest) requestConnectionParameters;
                connectionParameters = new JdbcConnectionParameters(jdbcConnectionParameters.url, jdbcConnectionParameters.user, jdbcConnectionParameters.password);
                break;
            case XML:
                XMlConnectionParametersRequest xmlRepoConnectionParameters = (XMlConnectionParametersRequest) requestConnectionParameters;
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
