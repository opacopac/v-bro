package com.tschanz.v_bro.repo.persistence.xml.node_parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;


class XmlNodeParserTest {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    private XmlNodeParser xmlNodeParser;


    @BeforeEach
    void setUp() {
        this.xmlNodeParser = new XmlNodeParser(XMLInputFactory.newFactory());
    }


    void initParser(String xmlText, String xmlElementName) throws XMLStreamException {
        var stream = new ByteArrayInputStream(xmlText.getBytes());
        this.xmlNodeParser.init(stream, xmlElementName);
    }


    @Test
    void nextNode_parses_attributes_and_value_elements_as_fields() throws XMLStreamException {
        var xmlText = XML_HEADER + "<doc><n0><n01>xxx</n01></n0><n1 id=\"ids__111\"><n11>yyy</n11></n1><n2 id=\"ids__222\">zzz</n2></doc>";
        this.initParser(xmlText, "n1");

        var node1 = this.xmlNodeParser.nextNode();

        assertNotNull(node1);
        assertEquals("n1", node1.getName());
        assertEquals(2, node1.getFields().size());
        assertTrue(node1.getFields().get(0).isAttribute());
        assertEquals("id", node1.getFields().get(0).getName());
        assertEquals("ids__111", node1.getFields().get(0).getValue());
        assertFalse(node1.getFields().get(1).isAttribute());
        assertEquals("n11", node1.getFields().get(1).getName());
        assertEquals("yyy", node1.getFields().get(1).getValue());
        assertEquals(0, node1.getChildNodes().size());
    }


    @Test
    void nextNode_parses_one_tag_elements_correctly() throws XMLStreamException {
        var xmlText = XML_HEADER + "<doc><n1 id=\"ids__111\" /></doc>";
        this.initParser(xmlText, "n1");

        var node1 = this.xmlNodeParser.nextNode();

        assertNotNull(node1);
        assertEquals("n1", node1.getName());
        assertEquals(1, node1.getFields().size());
        assertTrue(node1.getFields().get(0).isAttribute());
        assertEquals("id", node1.getFields().get(0).getName());
        assertEquals("ids__111", node1.getFields().get(0).getValue());
    }


    @Test
    void nextNode_called_repeatedly_returns_multiple_elements() throws XMLStreamException {
        var xmlText = XML_HEADER + "<doc><n1 id=\"ids__111\"></n1><n1 id=\"ids__112\"></n1><n2 id=\"ids__222\">zzz</n2></doc>";
        this.initParser(xmlText, "n1");

        var node1 = this.xmlNodeParser.nextNode();
        var node2 = this.xmlNodeParser.nextNode();
        var node3 = this.xmlNodeParser.nextNode();

        assertNotNull(node1);
        assertEquals("n1", node1.getName());
        assertEquals(1, node1.getFields().size());
        assertEquals("id", node1.getFields().get(0).getName());
        assertEquals("ids__111", node1.getFields().get(0).getValue());
        assertEquals(0, node1.getChildNodes().size());

        assertNotNull(node2);
        assertEquals("n1", node2.getName());
        assertEquals(1, node2.getFields().size());
        assertEquals("id", node2.getFields().get(0).getName());
        assertEquals("ids__112", node2.getFields().get(0).getValue());
        assertEquals(0, node2.getChildNodes().size());

        assertNull(node3);
    }


    @Test
    void nextNode_parses_mixed_fields_and_child_nodes() throws XMLStreamException {
        var xmlText = XML_HEADER + "\n" +
            "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">\n" +
            " <subsystemFQF>\n" +
            "  <partners>\n" +
            "   <partner id=\"ids__12004\">\n" +
            "    <partnerCode>11</partnerCode>\n" +
            "    <partnerKuerzel>SBB</partnerKuerzel>\n" +
            "    <druckBezeichnung><de>Schweizerische Bundesbahnen SBB</de><fr>Chemins de fer fédéraux suisses CFF</fr><it>Ferrovie federali svizzere FFS</it><en>Swiss Federal Railways</en></druckBezeichnung>\n" +
            "    <druckPartnerKuerzel><de>SBB</de><fr>CFF</fr><it>FFS</it><en>SBB</en></druckPartnerKuerzel>\n" +
            "    <bezeichnung>Schweizerische Bundesbahnen SBB</bezeichnung>\n" +
            "   </partner>\n" +
            " </subsystemFQF>\n" +
            "</ns2:datenrelease>";
        this.initParser(xmlText, "partner");

        var node1 = this.xmlNodeParser.nextNode();

        assertNotNull(node1);
        assertEquals("partner", node1.getName());
        assertEquals(4, node1.getFields().size());
        assertTrue(node1.getFields().get(0).isAttribute());
        assertEquals("id", node1.getFields().get(0).getName());
        assertEquals("ids__12004", node1.getFields().get(0).getValue());
        assertFalse(node1.getFields().get(1).isAttribute());
        assertEquals("partnerCode", node1.getFields().get(1).getName());
        assertEquals("11", node1.getFields().get(1).getValue());
        assertFalse(node1.getFields().get(2).isAttribute());
        assertEquals("partnerKuerzel", node1.getFields().get(2).getName());
        assertEquals("SBB", node1.getFields().get(2).getValue());
        assertFalse(node1.getFields().get(3).isAttribute());
        assertEquals("bezeichnung", node1.getFields().get(3).getName());
        assertEquals("Schweizerische Bundesbahnen SBB", node1.getFields().get(3).getValue());
        assertEquals(2, node1.getChildNodes().size());
        assertEquals("druckBezeichnung", node1.getChildNodes().get(0).getName());
        assertEquals(4, node1.getChildNodes().get(0).getFields().size());
        assertEquals("de", node1.getChildNodes().get(0).getFields().get(0).getName());
        assertEquals("Schweizerische Bundesbahnen SBB", node1.getChildNodes().get(0).getFields().get(0).getValue());
        assertEquals("fr", node1.getChildNodes().get(0).getFields().get(1).getName());
        assertEquals("Chemins de fer fédéraux suisses CFF", node1.getChildNodes().get(0).getFields().get(1).getValue());
        assertEquals("it", node1.getChildNodes().get(0).getFields().get(2).getName());
        assertEquals("Ferrovie federali svizzere FFS", node1.getChildNodes().get(0).getFields().get(2).getValue());
        assertEquals("en", node1.getChildNodes().get(0).getFields().get(3).getName());
        assertEquals("Swiss Federal Railways", node1.getChildNodes().get(0).getFields().get(3).getValue());
        assertEquals("druckPartnerKuerzel", node1.getChildNodes().get(1).getName());
        assertEquals(4, node1.getChildNodes().get(1).getFields().size());
        assertEquals("de", node1.getChildNodes().get(1).getFields().get(0).getName());
        assertEquals("SBB", node1.getChildNodes().get(1).getFields().get(0).getValue());
        assertEquals("fr", node1.getChildNodes().get(1).getFields().get(1).getName());
        assertEquals("CFF", node1.getChildNodes().get(1).getFields().get(1).getValue());
        assertEquals("it", node1.getChildNodes().get(1).getFields().get(2).getName());
        assertEquals("FFS", node1.getChildNodes().get(1).getFields().get(2).getValue());
        assertEquals("en", node1.getChildNodes().get(1).getFields().get(3).getName());
        assertEquals("SBB", node1.getChildNodes().get(1).getFields().get(3).getValue());
        assertEquals(0, node1.getChildNodes().get(0).getChildNodes().size());
    }
}
