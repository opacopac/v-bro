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
    public JdbcServerType getJdbcServerType() {
        if (this.currentConnectionUrl.trim().toLowerCase().startsWith("jdbc:mysql")) {
            return JdbcServerType.MYSQL;
        } else if (this.currentConnectionUrl.trim().toLowerCase().startsWith("jdbc:oracle")) {
            return JdbcServerType.ORACLE;
        } else {
            throw new IllegalArgumentException("unknown jdbc server type");
        }
    }


    @Override
    public void closeConnection() throws SQLException {
        this.currentConnection.close();
        this.currentConnection = null;
        this.currentConnectionUrl = null;
    }
}
