package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoConnectionService;
import com.tschanz.v_bro.repo.persistence.xml.service.XmlRepoService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class XmlRepoConnectionService implements RepoConnectionService {
    private final XmlRepoService xmlRepoService;
    private final XmlDataStructureService xmlDataStructureService;


    @Override
    public boolean isConnected() {
        return this.xmlRepoService.isConnected();
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        this.xmlRepoService.connect(parameters);
        this.xmlDataStructureService.readElementLut();
    }


    @Override
    public void disconnect() throws RepoException {
        this.xmlRepoService.disconnect();
    }
}
