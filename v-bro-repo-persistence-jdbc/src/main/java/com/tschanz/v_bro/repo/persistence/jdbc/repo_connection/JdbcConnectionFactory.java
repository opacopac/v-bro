package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import java.sql.Connection;
import java.sql.SQLException;


public interface JdbcConnectionFactory {
    void openConnection(String url, String user, String password, String schema) throws SQLException;

    Connection getCurrentConnection();

    String getCurrentSchema();

    JdbcServerType getJdbcServerType();

    void closeConnection() throws SQLException;
}
