package com.tschanz.v_bro.repo.jdbc.repo_connection;

import java.sql.Connection;
import java.sql.SQLException;


public interface JdbcConnectionFactory {
    void openConnection(String url, String user, String password) throws SQLException;

    Connection getCurrentConnection();

    boolean isCurrentConnectionMySql();

    void closeConnection() throws SQLException;
}
