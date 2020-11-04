package com.tschanz.v_bro.repo.persistence.xml.service;

import com.tschanz.v_bro.common.testing.MockHelper;
import com.tschanz.v_bro.common.testing.MockReturnValue;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.io.InputStream;


public class MockXmlRepoService extends XmlRepoService {
    public MockHelper<RepoException> mockHelper = new MockHelper<>();
    public MockReturnValue<Boolean> isConnectedResult = new MockReturnValue<>("isConnected");
    public MockReturnValue<InputStream> getNewXmlFileStreamResult = new MockReturnValue<>("getNewXmlFileStream");


    @Override
    public boolean isConnected() {
        this.mockHelper.reportMethodCall("isConnected");
        return this.isConnectedResult.next();
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.mockHelper.reportMethodCall("connect", parameters);
        this.mockHelper.checkThrowException();
    }


    @Override
    public void disconnect() throws RepoException {
        this.mockHelper.reportMethodCall("disconnect");
        this.mockHelper.checkThrowException();
    }


    @Override
    public InputStream getNewXmlFileStream() throws RepoException {
        this.mockHelper.reportMethodCall("getNewXmlFileStream");
        this.mockHelper.checkThrowException();
        return this.getNewXmlFileStreamResult.next();
    }
}
