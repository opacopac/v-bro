package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoConnectionService;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.sql.SQLException;


@Log
@RequiredArgsConstructor
public class JdbcRepoConnectionService implements RepoConnectionService {
    private final JdbcConnectionFactory connectionFactory;


    @Override
    public boolean isConnected() {
        return this.connectionFactory.getCurrentConnection() != null;
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        if (parameters.getClass() != JdbcConnectionParameters.class) {
            throw new RepoException("Only parameters of type JdbcConnectionParameters allowed for JDBC repos!");
        }

        JdbcConnectionParameters connectionParameters = (JdbcConnectionParameters) parameters;
        try {
            log.info("connecting to db");
            this.connectionFactory.openConnection(
                connectionParameters.getUrl(),
                connectionParameters.getUser(),
                connectionParameters.getPassword()
            );
        } catch (SQLException exception) {
            String msg = "error connecting to db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }
    }


    @Override
    public void disconnect() throws RepoException {
        log.info("disconnect from db");

        try {
            this.connectionFactory.closeConnection();
        } catch (SQLException exception) {
            String msg = "error disconnecting from db: " + exception.getMessage();
            log.severe(msg);
            throw new RepoException(msg, exception);
        }
    }
}
