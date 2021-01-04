package com.tschanz.v_bro.repo.persistence.xml.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.common.testing.SpyReturnValue;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.io.InputStream;


public class SpyXmlRepoConnectionService extends XmlRepoConnectionService {
    public SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    public SpyReturnValue<Boolean> isConnectedResult = new SpyReturnValue<>("isConnected");
    public SpyReturnValue<InputStream> getNewXmlFileStreamResult = new SpyReturnValue<>("getNewXmlFileStream");


    @Override
    public boolean isConnected() {
        this.spyHelper.reportMethodCall("isConnected");
        return this.isConnectedResult.next();
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.spyHelper.reportMethodCall("connect", parameters);
        this.spyHelper.checkThrowException();
    }


    @Override
    public void disconnect() throws RepoException {
        this.spyHelper.reportMethodCall("disconnect");
        this.spyHelper.checkThrowException();
    }
}
