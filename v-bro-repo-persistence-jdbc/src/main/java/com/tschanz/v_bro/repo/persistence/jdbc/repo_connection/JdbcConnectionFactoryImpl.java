package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@Getter
public class JdbcConnectionFactoryImpl implements JdbcConnectionFactory {
    private final static int FETCH_SIZE = 1000;

    private Connection currentConnection;
    private String currentConnectionUrl;
    private String currentSchema;


    @Override
    public void openConnection(String url, String user, String password, String schema) throws SQLException {
        this.currentConnection = DriverManager.getConnection(url, user, password);
        this.currentConnection.setSchema(schema);
        this.currentConnectionUrl = url;
        this.currentSchema = schema;
    }


    @Override
    public Statement createStatement() throws SQLException {
        var connection = this.getCurrentConnection();
        if (connection == null) {
            throw new SQLException("Connection is closed");
        }

        var statement = connection.createStatement();
        statement.setFetchSize(FETCH_SIZE);

        return statement;
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
        this.currentSchema = null;
    }
}
