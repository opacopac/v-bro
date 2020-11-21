package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementLutInfo;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class VersionLutParserTest {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    private ElementLutParser parser;


    @BeforeEach
    void setUp() {
        this.parser = new ElementLutParser();
    }


    @Test
    void readElementLut_finds_element_in_unversioned_entry() throws RepoException {
        String xmlText = XML_HEADER + "\n" +
            "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">\n" +
            " <subsystemFQF>\n" +
            "  <partners>\n" +
            "   <partner id=\"ids__12004\">\n" +
            "    <partnerCode>11</partnerCode>\n" +
            "    <partnerKuerzel>SBB</partnerKuerzel>\n" +
            "    <druckBezeichnung><de>Schweizerische Bundesbahnen SBB</de><fr>Schweizerische Bundesbahnen SBB</fr><it>Schweizerische Bundesbahnen SBB</it><en>Schweizerische Bundesbahnen SBB</en></druckBezeichnung>\n" +
            "    <druckPartnerKuerzel><de>SBB</de><fr>SBB</fr><it>SBB</it><en>SBB</en></druckPartnerKuerzel>\n" +
            "    <bezeichnung>Schweizerische Bundesbahnen SBB</bezeichnung>\n" +
            "    <screenBezeichnung><de>SBB</de><fr>SBB</fr><it>SBB</it><en>SBB</en></screenBezeichnung>\n" +
            "   </partner>\n" +
            "  </partners>\n" +
            " </subsystemFQF>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals("partner", result.get(0).getName());
        assertEquals("ids__12004", result.get(0).getElementId());
    }


    @Test
    void readElementLut_finds_element_in_versioned_entry() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">\n" +
            " <subsystemNetz>\n" +
            "  <betreibers>\n" +
            "   <betreiber id=\"ids__23361\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7724792684\">\n" +
            "     <name>743029</name>\n" +
            "     <abkuerzung>743029</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "  </betreibers>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals("betreiber", result.get(0).getName());
        assertEquals("ids__23361", result.get(0).getElementId());
    }


    @Test
    void readElementLut_finds_multiple_elements_with_different_names() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease>\n" +
            " <subsystemFQF>\n" +
            "  <partners>\n" +
            "   <partner id=\"ids__12004\">\n" +
            "    <partnerCode>11</partnerCode><partnerKuerzel>SBB</partnerKuerzel>\n" +
            "    <druckBezeichnung><de>Schweizerische Bundesbahnen SBB</de><fr>Schweizerische Bundesbahnen SBB</fr><it>Schweizerische Bundesbahnen SBB</it><en>Schweizerische Bundesbahnen SBB</en></druckBezeichnung>\n" +
            "    <druckPartnerKuerzel><de>SBB</de><fr>SBB</fr><it>SBB</it><en>SBB</en></druckPartnerKuerzel>\n" +
            "    <bezeichnung>Schweizerische Bundesbahnen SBB</bezeichnung>\n" +
            "    <screenBezeichnung><de>SBB</de><fr>SBB</fr><it>SBB</it><en>SBB</en></screenBezeichnung>\n" +
            "   </partner>\n" +
            "  </partners>\n" +
            " </subsystemFQF>\n" +
            " <subsystemNetz>\n" +
            "  <betreibers>\n" +
            "   <betreiber id=\"ids__23361\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7724792684\">\n" +
            "     <name>743029</name><abkuerzung>743029</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "  </betreibers>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals("partner", result.get(0).getName());
        assertEquals("ids__12004", result.get(0).getElementId());
        assertEquals("betreiber", result.get(1).getName());
        assertEquals("ids__23361", result.get(1).getElementId());
    }


    @Test
    void readElementLut_finds_multiple_elements_with_same_names() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease>\n" +
            " <subsystemFQF>\n" +
            "  <partners>\n" +
            "   <partner id=\"ids__12004\">\n" +
            "    <partnerCode>11</partnerCode><partnerKuerzel>SBB</partnerKuerzel>\n" +
            "    <druckBezeichnung><de>Schweizerische Bundesbahnen SBB</de><fr>Schweizerische Bundesbahnen SBB</fr><it>Schweizerische Bundesbahnen SBB</it><en>Schweizerische Bundesbahnen SBB</en></druckBezeichnung>\n" +
            "    <druckPartnerKuerzel><de>SBB</de><fr>SBB</fr><it>SBB</it><en>SBB</en></druckPartnerKuerzel>\n" +
            "    <bezeichnung>Schweizerische Bundesbahnen SBB</bezeichnung>\n" +
            "    <screenBezeichnung><de>SBB</de><fr>SBB</fr><it>SBB</it><en>SBB</en></screenBezeichnung>\n" +
            "   </partner>\n" +
            "   <partner id=\"ids__2598064\">\n" +
            "    <partnerCode>12</partnerCode>\n" +
            "    <partnerKuerzel>SBB RV</partnerKuerzel>\n" +
            "    <druckBezeichnung><de>SBB RV</de><fr>SBB RV</fr><it>SBB RV</it><en>SBB RV</en></druckBezeichnung>\n" +
            "    <druckPartnerKuerzel><de>SBB RV</de><fr>SBB RV</fr><it>SBB RV</it><en>SBB RV</en></druckPartnerKuerzel>\n" +
            "    <bezeichnung>Schweizerische Bundesbahnen SBB Regionalverkehr</bezeichnung>\n" +
            "    <screenBezeichnung><de>SBB RV</de><fr>SBB RV</fr><it>SBB RV</it><en>SBB RV</en></screenBezeichnung>\n" +
            "   </partner>\n" +
            "  </partners>\n" +
            " </subsystemFQF>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals("partner", result.get(0).getName());
        assertEquals("ids__12004", result.get(0).getElementId());
        assertEquals("partner", result.get(1).getName());
        assertEquals("ids__2598064", result.get(1).getElementId());
    }


    @Test
    void readElementLut_ignores_nested_nodes_with_ids() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease>\n" +
            " <subsystemNetz>\n" +
            "  <betreibers>\n" +
            "   <betreiber id=\"ids__23361\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7724792684\">\n" +
            "     <name>743029</name><abkuerzung>743029</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "   <betreiber id=\"ids__23395\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7724792549\">\n" +
            "     <name>743126</name>\n" +
            "     <abkuerzung>743126</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "  </betreibers>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals("betreiber", result.get(0).getName());
        assertEquals("ids__23361", result.get(0).getElementId());
        assertEquals("betreiber", result.get(1).getName());
        assertEquals("ids__23395", result.get(1).getElementId());
    }


    @Test
    void readElementStructure_finds_ids_and_idd() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease>\n" +
            " <subsystemNetz>\n" +
            "  <tarifnetzKanten>\n" +
            "   <tarifnetzKanten id=\"idd__9029\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"idd__9030\">\n" +
            "     <haltestelle1>ids__36628</haltestelle1>\n" +
            "     <haltestelle2>ids__47352</haltestelle2>\n" +
            "     <verwaltung>ids__4057128422</verwaltung>\n" +
            "     <verkehrsmittelTyp>BAHN</verkehrsmittelTyp>\n" +
            "     <zuschlagspflichtig>false</zuschlagspflichtig>\n" +
            "     <bahnersatz>false</bahnersatz>\n" +
            "    </version>\n" +
            "   </tarifnetzKanten>\n" +
            "  </tarifnetzKanten>\n" +
            "  <verwaltungen>\n" +
            "   <verwaltung id=\"ids__4057249110\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7949738426\">\n" +
            "     <verwaltungsCode>sbg034</verwaltungsCode>\n" +
            "     <betreiber>ids__23025</betreiber>\n" +
            "    </version>\n" +
            "   </verwaltung>\n" +
            "  </verwaltungen>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals("tarifnetzKanten", result.get(0).getName());
        assertEquals("idd__9029", result.get(0).getElementId());
        assertEquals("verwaltung", result.get(1).getName());
        assertEquals("ids__4057249110", result.get(1).getElementId());
    }


    @Test
    void readElementLut_finds_element_position_of_document_node() throws RepoException {
        //                        5    5    6          7          8         9        10
        //                        4    56789012345 678901234 5678901234567890123456789012
        String xmlText = XML_HEADER + "<node1 id=\"ids__123\"><node2>text</node2></node1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(55, result.get(0).getStartBytePos());
        assertEquals(102, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_of_child_node() throws RepoException {
        //                        5    5    6         7          8          9        10        11
        //                        4    567890123456789012 345678901 234567890123456789012345678901234567
        String xmlText = XML_HEADER + "<node1><node2 id=\"ids__123\"><node3>text</node3></node2></node1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(62, result.get(0).getStartBytePos());
        assertEquals(109, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_with_spaces() throws RepoException {
        //                        5    5    6         7          8          9        10        11        12
        //                        4    5678901234567890123456 789012345 678901234567890123456789012345678901234567
        String xmlText = XML_HEADER + "  <node1>  <node2 id=\"ids__123\">  <node3>text</node3>  </node2>  </node1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(66, result.get(0).getStartBytePos());
        assertEquals(117, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_of_2nd_child_node() throws RepoException {
        //                        5    5    6         7          8          9        10        11        12          13        14        15        16
        //                        4    567890123456789012 345678901 234567890123456789012345678901234567890 123456789 012345678901234567890123456789012345
        String xmlText = XML_HEADER + "<node1><node2 id=\"ids__123\"><node3>text</node3></node2><node2 id=\"ids__124\"><node3>text</node3></node2></node1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals(110, result.get(1).getStartBytePos());
        assertEquals(157, result.get(1).getEndBytePos());
    }

    @Test
    void readElementLut_finds_element_position_with_other_node_before_element() throws RepoException {
        //                        5    5    6         7          8          9        10        11
        //                        4    567890123456789012345678 901234567 890123456789012345678901
        String xmlText = XML_HEADER + "<n1><n4>txt</n4><n2 id=\"ids__123\"><n3>text</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(71, result.get(0).getStartBytePos());
        assertEquals(106, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_having_a_2byte_char_within_element() throws RepoException {
        //                        5    5    6          7          8  !     9
        //                        4    567890123456 789012345 67890124567890123456789
        String xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><n3>Züg</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(59, result.get(0).getStartBytePos());
        assertEquals(94, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_having_a_2byte_char_before_element() throws RepoException {
        //                        5    5    6   !    7          8          9        10        11
        //                        4    567890123467890123456789 012345678 901234567890123456789012
        String xmlText = XML_HEADER + "<n1><n4>Tür</n4><n2 id=\"ids__123\"><n3>text</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(72, result.get(0).getStartBytePos());
        assertEquals(107, result.get(0).getEndBytePos());
    }

    @Test
    void readElementLut_finds_element_positions_having_a_2byte_char_in_various_places() throws RepoException {
        //                        5    5    6   !  !7         8          9         10  !    11        12  !    13         14         15        16        17         18         19 !     20        21
        //                        4    5678901234678012345678901234 567890123 4567890123567890123456789012356789012345678 901234567 890123456789012345678901234 567890123 45678901245678901234567890
        String xmlText = XML_HEADER + "<n1><n4>Réclère</n4><n2 id=\"ids__123\"><n3>Gruyères</n3></n2><n4>hüa</n4><n2 id=\"ids__124\"><n3>text</n3></n2><n2 id=\"ids__125\"><n3>retäxt</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(3, result.size());
        assertEquals(77, result.get(0).getStartBytePos());
        assertEquals(117, result.get(0).getEndBytePos());
        assertEquals(131, result.get(1).getStartBytePos());
        assertEquals(166, result.get(1).getEndBytePos());
        assertEquals(167, result.get(2).getStartBytePos());
        assertEquals(205, result.get(2).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_having_an_ampersand_within_element() throws RepoException {
        //                        5    5    6          7          8         9        10        11
        //                        4    567890123456 789012345 67890123456789012345678901234567890123
        String xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><n3>Abo &amp; Billette</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(59, result.get(0).getStartBytePos());
        assertEquals(108, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_element_position_having_an_ampersand_before_element() throws RepoException {
        //                        5    5    6         7         8          9         10        11        12
        //                        4    567890123456789012345678901234567 890123456 789012345678901234567890
        String xmlText = XML_HEADER + "<n1><n4>Tuer&amp;Tor</n4><n2 id=\"ids__123\"><n3>text</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals(80, result.get(0).getStartBytePos());
        assertEquals(115, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_ignores_inner_nodes_with_ids() throws RepoException {
        //                        5    5    6          7          8          9         10        11         12         13        14
        //                        4    567890123456 789012345 678901234 567890123 45678901234567890123456 789012345 67890123456789012
        String xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><n3 id=\"ids__124\">text</n3></n2><n4 id=\"ids__125\">text</n4></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals("n2", result.get(0).getName());
        assertEquals("ids__123", result.get(0).getElementId());
        assertEquals(59, result.get(0).getStartBytePos());
        assertEquals(108, result.get(0).getEndBytePos());
        assertEquals("n4", result.get(1).getName());
        assertEquals("ids__125", result.get(1).getElementId());
        assertEquals(109, result.get(1).getStartBytePos());
        assertEquals(135, result.get(1).getEndBytePos());
    }


    @Test
    void readElementLut_finds_single_tag_elements() throws RepoException {
        //                        5    5    6          7          8
        //                        4    567890123456 789012345 6789012
        String xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"/></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals("n2", result.get(0).getName());
        assertEquals("ids__123", result.get(0).getElementId());
        assertEquals(59, result.get(0).getStartBytePos());
        assertEquals(77, result.get(0).getEndBytePos());
    }


    @Test
    void readElementLut_finds_elements_after_single_tag_nodes() throws RepoException {
        //                        5    5    6          7          8          9         10
        //                        4    56789012345678 90123 4567890123 456789012 345678901234567
        String xmlText = XML_HEADER + "<n1><n3 name=\"self\"/><n2 id=\"ids__123\">text</n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals("n2", result.get(0).getName());
        assertEquals("ids__123", result.get(0).getElementId());
        assertEquals(76, result.get(0).getStartBytePos());
        assertEquals(102, result.get(0).getEndBytePos());
    }

    @Test
    void readElementLut_finds_elements_after_comments() throws RepoException {
        //                        5    5    6         7          8         9         10        11         12         13        14
        //                        4    56789012345678901234567 890123456 78901234567890123456789012345 678901234 5678901234567890123
        String xmlText = XML_HEADER + "<n1><!-- c1 --><n2 id=\"ids__123\">text</n2><!-- c2 --><n2 id=\"ids__124\">text</n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(2, result.size());
        assertEquals("n2", result.get(0).getName());
        assertEquals("ids__123", result.get(0).getElementId());
        assertEquals(70, result.get(0).getStartBytePos());
        assertEquals(96, result.get(0).getEndBytePos());
        assertEquals("n2", result.get(1).getName());
        assertEquals("ids__124", result.get(1).getElementId());
        assertEquals(108, result.get(1).getStartBytePos());
        assertEquals(134, result.get(1).getEndBytePos());
    }


    @Test
    void readElementLut_finds_elements_with_comments_inside() throws RepoException {
        //                        5    5    6          7          8         9        10        11        12
        //                        4    567890123456 789012345 6789012345678901234567890123456789012345678901
        String xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><!-- c1 --><n3>text</n3></n2></n1>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<XmlElementLutInfo> result = this.parser.readElementLut(stream);

        assertEquals(1, result.size());
        assertEquals("n2", result.get(0).getName());
        assertEquals("ids__123", result.get(0).getElementId());
        assertEquals(59, result.get(0).getStartBytePos());
        assertEquals(105, result.get(0).getEndBytePos());
    }
}

