package com.tschanz.v_bro.data_structure.persistence.xml.service;

import com.tschanz.v_bro.repo.persistence.xml.parser.XmlIdElementPosInfo;
import com.tschanz.v_bro.repo.persistence.xml.parser.XmlIdRefParser;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;

import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class XmlRepoService implements RepoService {
    // TODO => config file
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


    public Map<String, XmlIdElementPosInfo> getElementLut() throws RepoException {
        if (this.elementStructureMap == null) {
            this.readElementLut();
        }

        return this.elementStructureMap;
    }


    public InputStream getElementClassInputStream(String elementClassName) throws RepoException {
        var elementLuts = this.getElementLut().values()
            .stream()
            .filter(element -> elementClassName.equals(element.getName()))
            .sorted(Comparator.comparingInt(XmlIdElementPosInfo::getStartBytePos))
            .collect(Collectors.toList());

        return this.getNewXmlFileStream(elementLuts.get(0).getStartBytePos(), elementLuts.get(elementLuts.size() - 1).getEndBytePos());
    }


    public InputStream getElementInputStream(String elementId) throws RepoException {
        var elementLutInfo = this.getElementLut().get(elementId);
        if (elementLutInfo == null) {
            throw new IllegalArgumentException(String.format("element id '%s' not found in xml lookup table", elementId));
        }

        return this.getNewXmlFileStream(elementLutInfo.getStartBytePos(), elementLutInfo.getEndBytePos());
    }


    private InputStream getNewXmlFileStream(int startBytePos, int endBytePos) throws RepoException {
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


    private InputStream getNewXmlFileStream() throws RepoException {
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


    // TODO: temp => read from xml file
    private byte[] wrapInRootNodeAndAddPreamble(byte[] bytes) {
        byte[] result = new byte[XML_PREAMBLE.length() + ROOT_NODE_START.length() + bytes.length + ROOT_NODE_END.length()];

        System.arraycopy(XML_PREAMBLE.getBytes(), 0, result, 0, XML_PREAMBLE.length());
        System.arraycopy(ROOT_NODE_START.getBytes(), 0, result, XML_PREAMBLE.length(), ROOT_NODE_START.length());
        System.arraycopy(bytes, 0, result, XML_PREAMBLE.length() + ROOT_NODE_START.length(), bytes.length);
        System.arraycopy(ROOT_NODE_END.getBytes(), 0, result, XML_PREAMBLE.length() + ROOT_NODE_START.length() + bytes.length, ROOT_NODE_END.length());

        return result;
    }


    private void readElementLut() throws RepoException {
        var xmlFileStream = this.getNewXmlFileStream();
        var parser = new XmlIdRefParser(
            xmlFileStream,
            ID_ATTRIBUTE_NAME,
            List.of(ID_VALUE_PREFIX_1, ID_VALUE_PREFIX_2)
        );
        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        this.elementStructureMap = new HashMap<>();
        idElements.forEach(element -> this.elementStructureMap.put(element.getElementId(), element));
    }


    public static String findId(XMLStreamReader reader) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if (reader.getAttributeLocalName(i).equals(XmlRepoService.ID_ATTRIBUTE_NAME)) {
                String value = reader.getAttributeValue(i);
                if (isId(value)) {
                    return value;
                }
            }
        }

        return null;
    }


    public static LocalDate findDate(XMLStreamReader reader, String attributeName) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if (reader.getAttributeLocalName(i).equals(attributeName)) {
                String dateString = reader.getAttributeValue(i);
                return LocalDate.parse(dateString);
            }
        }

        return null;
    }


    public static boolean isId(String value) {
        if (value == null) {
            return false;
        }

        return (value.startsWith(XmlRepoService.ID_VALUE_PREFIX_1) || value.startsWith(XmlRepoService.ID_VALUE_PREFIX_2));
    }
}
