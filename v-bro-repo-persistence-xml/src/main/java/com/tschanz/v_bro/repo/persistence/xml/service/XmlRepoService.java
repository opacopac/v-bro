package com.tschanz.v_bro.repo.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.persistence.xml.idref_parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;

import java.io.*;
import java.util.Map;


public class XmlRepoService implements RepoService {
    // TODO: temp => read from xml real file
    private static final String XML_PREAMBLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    private static final String ROOT_NODE_START = "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">";
    private static final String ROOT_NODE_END = "</ns2:datenrelease>";

    private XmlConnectionParameters connectionParameters;
    private Map<String, XmlIdElementPosInfo> elementStructureMap;


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
        var xmlFile = new File(connectionParameters.getFilename());
        if (!xmlFile.exists()) {
            throw new RepoException("File not found: " + connectionParameters.getFilename());
        }
    }


    @Override
    public void disconnect() throws RepoException {
        if (!this.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        this.connectionParameters = null;
    }


    public InputStream getNewXmlFileStream() throws RepoException {
        if (!this.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        BufferedInputStream xmlFileStream;
        try {
            xmlFileStream = new BufferedInputStream(new FileInputStream(this.connectionParameters.getFilename()));
        } catch (FileNotFoundException exception) {
            throw new RepoException(exception.getMessage(), exception);
        }

        return xmlFileStream;
    }


    public InputStream getNewXmlFileStream(int startBytePos, int endBytePos) throws RepoException {
        if (!this.isConnected()) {
            throw new RepoException("Repo not connected!");
        }

        if (startBytePos > endBytePos) {
            throw new IllegalArgumentException("start position must be smaller than end position");
        }

        byte[] bytes;
        try {
            var xmlFileStream = new FileInputStream(this.connectionParameters.getFilename());
            xmlFileStream.skip(startBytePos);
            bytes = xmlFileStream.readNBytes(endBytePos - startBytePos + 1);
            xmlFileStream.close();

        } catch (IOException exception) {
            throw new RepoException(exception.getMessage(), exception);
        }

        return new ByteArrayInputStream(
            this.wrapInRootNodeAndAddPreamble(bytes)
        );
    }


    // TODO: temp => read from xml file
    private byte[] wrapInRootNodeAndAddPreamble(byte[] bytes) {
        byte[] result = new byte[XML_PREAMBLE.length() + ROOT_NODE_START.length() + bytes.length + ROOT_NODE_END.length()];

        System.arraycopy(XML_PREAMBLE.getBytes(), 0, result, 0, XML_PREAMBLE.length());
        System.arraycopy(ROOT_NODE_START.getBytes(), 0, result, XML_PREAMBLE.length(), ROOT_NODE_START.length());
        System.arraycopy(bytes, 0, result, XML_PREAMBLE.length() + ROOT_NODE_START.length(), bytes.length);
        System.arraycopy(ROOT_NODE_END.getBytes(), 0, result, XML_PREAMBLE.length() + ROOT_NODE_START.length() + bytes.length, ROOT_NODE_END.length());

        return result;
    }
}
