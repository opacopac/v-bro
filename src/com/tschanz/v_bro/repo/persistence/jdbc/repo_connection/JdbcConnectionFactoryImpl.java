package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JdbcConnectionFactoryImpl implements JdbcConnectionFactory {
    private Connection currentConnection;
    private String currentConnectionUrl;


    @Override
    public void openConnection(String url, String user, String password) throws SQLException {
        this.currentConnection = DriverManager.getConnection(url, user, password);
        this.currentConnectionUrl = url;
    }


    @Override
    public Connection getCurrentConnection() {
        return this.currentConnection;
    }


    @Override
    public boolean isCurrentConnectionMySql() {
        if (this.getCurrentConnection() == null) {
            return false;
        }

        return this.currentConnectionUrl.toLowerCase().startsWith("jdbc:mysql");
    }


    @Override
    public void closeConnection() throws SQLException {
        this.currentConnection.close();
        this.currentConnection = null;
        this.currentConnectionUrl = null;
    }
}
