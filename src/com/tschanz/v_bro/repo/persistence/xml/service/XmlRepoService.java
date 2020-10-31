package com.tschanz.v_bro.repo.persistence.xml.service;

import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;

import java.io.*;


public class XmlRepoService implements RepoService {
    public static final String ID_ATTRIBUTE_NAME = "id";
    public static final String ID_VALUE_PREFIX_1 = "idd__";
    public static final String ID_VALUE_PREFIX_2 = "ids__";
    public static final String VERSION_NODE_NAME = "version";
    public static final String VERSION_VON_ATTRIBUTE_NAME = "gueltigVon";
    public static final String VERSION_BIS_ATTRIBUTE_NAME = "gueltigBis";
    // TODO: temp => read from xml real file
    private static final String XML_PREAMBLE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    private static final String ROOT_NODE_START = "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">";
    private static final String ROOT_NODE_END = "</ns2:datenrelease>";

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
        BufferedInputStream xmlFileStream;
        try {
            xmlFileStream = new BufferedInputStream(new FileInputStream(this.connectionParameters.getFilename()));
        } catch (FileNotFoundException exception) {
            throw new RepoException(exception.getMessage(), exception);
        }

        return xmlFileStream;
    }


    public InputStream getNewXmlFileStream(int startBytePos, int endBytePos) throws RepoException {
        byte[] bytes;
        try {
            FileInputStream xmlFileStream = new FileInputStream(this.connectionParameters.getFilename());
            long skippedBytes = xmlFileStream.skip(startBytePos);
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


    public FileReader getNewXmlFileReader() throws RepoException {
        FileReader reader;

        try {
            reader = new FileReader(this.connectionParameters.getFilename());
        } catch (FileNotFoundException exception) {
            throw new RepoException(exception);
        }

        return reader;
    }
}
