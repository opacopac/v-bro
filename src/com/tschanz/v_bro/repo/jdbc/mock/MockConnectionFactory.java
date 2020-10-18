package com.tschanz.v_bro.repo.jdbc.mock;

import com.tschanz.v_bro.common.test.MockHelper;
import com.tschanz.v_bro.repo.jdbc.service.JdbcConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class MockConnectionFactory implements JdbcConnectionFactory {
    public MockHelper<SQLException> mockHelper = new MockHelper<>();
    public MockConnection mockConnection = new MockConnection();
    public MockConnection getCurrentConnectionResult;
    public boolean isCurrentConnectionMySqlResult;


    public MockConnectionFactory(boolean openConnection) {
        if (openConnection) {
            this.getCurrentConnectionResult = mockConnection;
        }
    }


    @Override
    public void openConnection(String url, String user, String password) throws SQLException {
        this.mockHelper.reportMethodCall("openConnection", url, user, password);
        this.mockHelper.checkThrowException();
        this.getCurrentConnectionResult = mockConnection;
    }


    @Override
    public Connection getCurrentConnection() {
        return this.getCurrentConnectionResult;
    }


    @Override
    public boolean isCurrentConnectionMySql() {
        return this.isCurrentConnectionMySqlResult;
    }


    @Override
    public void closeConnection() throws SQLException {
        this.mockHelper.reportMethodCall("closeConnection");
        this.mockHelper.checkThrowException();
        this.getCurrentConnectionResult = null;
    }
}
