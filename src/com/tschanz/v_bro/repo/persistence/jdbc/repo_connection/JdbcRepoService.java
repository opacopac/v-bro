package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;

import java.sql.SQLException;
import java.util.logging.Logger;


public class JdbcRepoService implements RepoService {
    private final Logger logger = Logger.getLogger(JdbcRepoService.class.getName());
    private final JdbcConnectionFactory connectionFactory;


    public JdbcRepoService(JdbcConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


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
            this.logger.info("connecting to db");
            this.connectionFactory.openConnection(
                connectionParameters.getUrl(),
                connectionParameters.getUser(),
                connectionParameters.getPassword()
            );
        } catch (SQLException exception) {
            String msg = "error connecting to db: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }
    }


    @Override
    public void disconnect() throws RepoException {
        this.logger.info("disconnect from db");

        try {
            this.connectionFactory.closeConnection();
        } catch (SQLException exception) {
            String msg = "error disconnecting from db: " + exception.getMessage();
            this.logger.severe(msg);
            throw new RepoException(msg, exception);
        }
    }
}
