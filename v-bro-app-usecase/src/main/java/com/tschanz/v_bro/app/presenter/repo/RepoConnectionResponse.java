package com.tschanz.v_bro.app.presenter.repo;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class RepoConnectionResponse {
    public final RepoType repoType;


    public static RepoConnectionResponse fromDomain(ConnectionParameters connectionParameters) {
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
}
