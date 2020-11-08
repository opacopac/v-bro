package com.tschanz.v_bro.repo.persistence.jdbc.mock;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;

import java.sql.Connection;
import java.sql.SQLException;


public class SpyConnectionFactory implements JdbcConnectionFactory {
    public SpyHelper<SQLException> spyHelper = new SpyHelper<>();
    public SpyConnection spyConnection = new SpyConnection();
    public SpyConnection getCurrentConnectionResult;
    public JdbcServerType jdbcServerType = JdbcServerType.ORACLE;


    public SpyConnectionFactory(boolean openConnection) {
        if (openConnection) {
            this.getCurrentConnectionResult = spyConnection;
        }
    }


    @Override
    public void openConnection(String url, String user, String password) throws SQLException {
        this.spyHelper.reportMethodCall("openConnection", url, user, password);
        this.spyHelper.checkThrowException();
        this.getCurrentConnectionResult = spyConnection;
    }


    @Override
    public Connection getCurrentConnection() {
        return this.getCurrentConnectionResult;
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
