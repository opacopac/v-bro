package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class SpyConnectionFactory implements JdbcConnectionFactory {
    public SpyHelper<SQLException> spyHelper = new SpyHelper<>();
    public SpyConnection spyConnection = new SpyConnection();
    public SpyStatement spyStatement = new SpyStatement();
    public SpyConnection getCurrentConnectionResult;
    public String getCurrentSchemaResult;
    public JdbcServerType jdbcServerType = JdbcServerType.ORACLE;


    public SpyConnectionFactory(boolean openConnection) {
        if (openConnection) {
            this.getCurrentConnectionResult = spyConnection;
        }
    }


    @Override
    public void openConnection(String url, String user, String password, String schema) throws SQLException {
        this.spyHelper.reportMethodCall("openConnection", url, user, password, schema);
        this.spyHelper.checkThrowException();
        this.getCurrentConnectionResult = spyConnection;
    }


    @Override
    public Connection getCurrentConnection() {
        return this.getCurrentConnectionResult;
    }


    @Override
    public Statement createStatement() throws SQLException {
        return this.spyStatement;
    }


    @Override
    public String getCurrentSchema() {
        return this.getCurrentSchemaResult;
    }


    @Override
    public JdbcServerType getJdbcServerType() {
        return this.jdbcServerType;
    }


    @Override
    public void closeConnection() throws SQLException {
        this.spyHelper.reportMethodCall("closeConnection");
        this.spyHelper.checkThrowException();
        this.getCurrentConnectionResult = null;
    }
}
