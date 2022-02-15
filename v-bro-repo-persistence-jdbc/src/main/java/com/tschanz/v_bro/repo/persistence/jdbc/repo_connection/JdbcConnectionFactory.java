package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public interface JdbcConnectionFactory {
    void openConnection(String url, String user, String password, String schema) throws SQLException;

    Connection getCurrentConnection();

    Statement createStatement() throws SQLException;

    String getCurrentSchema();

    JdbcServerType getJdbcServerType();

    void closeConnection() throws SQLException;
}
