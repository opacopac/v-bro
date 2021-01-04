package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoConnectionService;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoConnectionService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class XmlRepoConnectionServiceWrapper implements RepoConnectionService {
    private final XmlRepoConnectionService xmlRepoConnectionService;
    private final XmlDataStructureService xmlDataStructureService;


    @Override
    public boolean isConnected() {
        return this.xmlRepoConnectionService.isConnected();
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.xmlRepoConnectionService.connect(parameters);
        this.xmlDataStructureService.readElementLut();
    }


    @Override
    public void disconnect() throws RepoException {
        this.xmlRepoConnectionService.disconnect();
    }
}
