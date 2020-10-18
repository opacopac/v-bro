package com.tschanz.v_bro.repo.xml.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.xml.model.XmlConnectionParameters;

import java.io.*;


public class XmlRepoService implements RepoService {
    public static final String ID_ATTRIBUTE_NAME = "id";
    public static final String ID_VALUE_PREFIX_1 = "idd__";
    public static final String ID_VALUE_PREFIX_2 = "ids__";
    public static final String VERSION_ELEMENT_NAME = "version";
    public static final String VERSION_VON_ATTRIBUTE_NAME = "gueltigVon";
    public static final String VERSION_BIS_ATTRIBUTE_NAME = "gueltigBis";

    private XmlConnectionParameters connectionParameters;


    @Override
    public boolean isConnected() {
        return this.connectionParameters != null;
    }


    @Override
    public void connect(ConnectionParameters parameters) throws RepoException {
        if (parameters.getClass() != XmlConnectionParameters.class) {
            throw new RepoException("Only parameters of type XmlConnectionParameters allowed for XML repos!");
        }

        this.connectionParameters = ((XmlConnectionParameters) parameters);
        File xmlFile = new File(connectionParameters.getFilename());
        if (!xmlFile.exists()) {
            throw new RepoException("File not found: " + connectionParameters.getFilename());
        }
    }


    @Override
    public void disconnect() throws RepoException {
        this.connectionParameters = null;
    }


    public InputStream getNewXmlFileStream() throws RepoException {
        FileInputStream xmlFileStream;
        try {
            xmlFileStream = new FileInputStream(this.connectionParameters.getFilename());
        } catch (FileNotFoundException exception) {
            throw new RepoException(exception.getMessage(), exception);
        }

        return xmlFileStream;
    }
}
