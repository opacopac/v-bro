package com.tschanz.v_bro.repo.persistence.xml.idref_parser;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class XmlIdRefParserTest {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";


    @BeforeEach
    void setUp() {
    }


    XmlIdRefParser createParser(String xmlText) {
        var stream = new ByteArrayInputStream(xmlText.getBytes());
        return new XmlIdRefParser(
            stream,
            "id",
            List.of("ids__", "idd__")
        );
    }


    @Test
    void parse_finds_element_in_unversioned_entry() throws RepoException {
        var xmlText = XML_HEADER + "\n" +
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
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        assertEquals(1, idElements.size());
        assertEquals("partner", idElements.get(0).getName());
        assertEquals("ids__12004", idElements.get(0).getElementId());
        assertEquals(0, idRefs.size());
    }


    @Test
    void parse_finds_element_in_versioned_entry() throws RepoException {
        var xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        assertEquals(1, idElements.size());
        assertEquals("betreiber", idElements.get(0).getName());
        assertEquals("ids__23361", idElements.get(0).getElementId());
        assertEquals(0, idRefs.size());
    }


    @Test
    void parse_finds_multiple_elements_with_different_names() throws RepoException {
        var xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        assertEquals(2, idElements.size());
        assertEquals("partner", idElements.get(0).getName());
        assertEquals("ids__12004", idElements.get(0).getElementId());
        assertEquals("betreiber", idElements.get(1).getName());
        assertEquals("ids__23361", idElements.get(1).getElementId());
        assertEquals(0, idRefs.size());
    }


    @Test
    void parse_finds_multiple_elements_with_same_names() throws RepoException {
        var xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        assertEquals(2, idElements.size());
        assertEquals("partner", idElements.get(0).getName());
        assertEquals("ids__12004", idElements.get(0).getElementId());
        assertEquals("partner", idElements.get(1).getName());
        assertEquals("ids__2598064", idElements.get(1).getElementId());
        assertEquals(0, idRefs.size());
    }


    @Test
    void parse_ignores_nested_nodes_with_ids() throws RepoException {
        var xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        assertEquals(2, idElements.size());
        assertEquals("betreiber", idElements.get(0).getName());
        assertEquals("ids__23361", idElements.get(0).getElementId());
        assertEquals("betreiber", idElements.get(1).getName());
        assertEquals("ids__23395", idElements.get(1).getElementId());
        assertEquals(0, idRefs.size());
    }


    @Test
    void readElementStructure_finds_ids_and_idd() throws RepoException {
        var xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
            "     <betreiber>idd__23025</betreiber>\n" +
            "    </version>\n" +
            "   </verwaltung>\n" +
            "  </verwaltungen>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();
        var idRefs = parser.getIdRefPositions();

        assertEquals(2, idElements.size());
        assertEquals("tarifnetzKanten", idElements.get(0).getName());
        assertEquals("idd__9029", idElements.get(0).getElementId());
        assertEquals("verwaltung", idElements.get(1).getName());
        assertEquals("ids__4057249110", idElements.get(1).getElementId());

        assertEquals(4, idRefs.size());
        assertEquals("ids__36628", idRefs.get(0).getIdRef());
        assertEquals("ids__47352", idRefs.get(1).getIdRef());
        assertEquals("ids__4057128422", idRefs.get(2).getIdRef());
        assertEquals("idd__23025", idRefs.get(3).getIdRef());
    }


    @Test
    void parse_finds_element_position_of_document_node() throws RepoException {
        //                     5    5    6          7          8         9        10
        //                     4    56789012345 678901234 5678901234567890123456789012
        var xmlText = XML_HEADER + "<node1 id=\"ids__123\"><node2>text</node2></node1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(55, idElements.get(0).getStartBytePos());
        assertEquals(102, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_of_child_node() throws RepoException {
        //                     5    5    6         7          8          9        10        11
        //                     4    567890123456789012 345678901 234567890123456789012345678901234567
        var xmlText = XML_HEADER + "<node1><node2 id=\"ids__123\"><node3>text</node3></node2></node1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(62, idElements.get(0).getStartBytePos());
        assertEquals(109, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_with_spaces() throws RepoException {
        //                     5    5    6         7          8          9        10        11        12
        //                     4    5678901234567890123456 789012345 678901234567890123456789012345678901234567
        var xmlText = XML_HEADER + "  <node1>  <node2 id=\"ids__123\">  <node3>text</node3>  </node2>  </node1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(66, idElements.get(0).getStartBytePos());
        assertEquals(117, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_of_2nd_child_node() throws RepoException {
        //                     5    5    6         7          8          9        10        11        12          13        14        15        16
        //                     4    567890123456789012 345678901 234567890123456789012345678901234567890 123456789 012345678901234567890123456789012345
        var xmlText = XML_HEADER + "<node1><node2 id=\"ids__123\"><node3>text</node3></node2><node2 id=\"ids__124\"><node3>text</node3></node2></node1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(2, idElements.size());
        assertEquals(110, idElements.get(1).getStartBytePos());
        assertEquals(157, idElements.get(1).getEndBytePos());
    }

    @Test
    void parse_finds_element_position_with_other_node_before_element() throws RepoException {
        //                     5    5    6         7          8          9        10        11
        //                     4    567890123456789012345678 901234567 890123456789012345678901
        var xmlText = XML_HEADER + "<n1><n4>txt</n4><n2 id=\"ids__123\"><n3>text</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(71, idElements.get(0).getStartBytePos());
        assertEquals(106, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_having_a_2byte_char_within_element() throws RepoException {
        //                     5    5    6          7          8  !     9
        //                     4    567890123456 789012345 67890124567890123456789
        var xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><n3>Züg</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(59, idElements.get(0).getStartBytePos());
        assertEquals(94, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_having_a_2byte_char_before_element() throws RepoException {
        //                     5    5    6   !    7          8          9        10        11
        //                     4    567890123467890123456789 012345678 901234567890123456789012
        var xmlText = XML_HEADER + "<n1><n4>Tür</n4><n2 id=\"ids__123\"><n3>text</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(72, idElements.get(0).getStartBytePos());
        assertEquals(107, idElements.get(0).getEndBytePos());
    }

    @Test
    void parse_finds_element_positions_having_a_2byte_char_in_various_places() throws RepoException {
        //                     5    5    6   !  !7         8          9         10  !    11        12  !    13         14         15        16        17         18         19 !     20        21
        //                     4    5678901234678012345678901234 567890123 4567890123567890123456789012356789012345678 901234567 890123456789012345678901234 567890123 45678901245678901234567890
        var xmlText = XML_HEADER + "<n1><n4>Réclère</n4><n2 id=\"ids__123\"><n3>Gruyères</n3></n2><n4>hüa</n4><n2 id=\"ids__124\"><n3>text</n3></n2><n2 id=\"ids__125\"><n3>retäxt</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(3, idElements.size());
        assertEquals(77, idElements.get(0).getStartBytePos());
        assertEquals(117, idElements.get(0).getEndBytePos());
        assertEquals(131, idElements.get(1).getStartBytePos());
        assertEquals(166, idElements.get(1).getEndBytePos());
        assertEquals(167, idElements.get(2).getStartBytePos());
        assertEquals(205, idElements.get(2).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_having_an_ampersand_within_element() throws RepoException {
        //                     5    5    6          7          8         9        10        11
        //                     4    567890123456 789012345 67890123456789012345678901234567890123
        var xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><n3>Abo &amp; Billette</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(59, idElements.get(0).getStartBytePos());
        assertEquals(108, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_element_position_having_an_ampersand_before_element() throws RepoException {
        //                     5    5    6         7         8          9         10        11        12
        //                     4    567890123456789012345678901234567 890123456 789012345678901234567890
        var xmlText = XML_HEADER + "<n1><n4>Tuer&amp;Tor</n4><n2 id=\"ids__123\"><n3>text</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals(80, idElements.get(0).getStartBytePos());
        assertEquals(115, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_ignores_inner_nodes_with_ids() throws RepoException {
        //                     5    5    6          7          8          9         10        11         12         13        14
        //                     4    567890123456 789012345 678901234 567890123 45678901234567890123456 789012345 67890123456789012
        var xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><n3 id=\"ids__124\">text</n3></n2><n4 id=\"ids__125\">text</n4></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(2, idElements.size());
        assertEquals("n2", idElements.get(0).getName());
        assertEquals("ids__123", idElements.get(0).getElementId());
        assertEquals(59, idElements.get(0).getStartBytePos());
        assertEquals(108, idElements.get(0).getEndBytePos());
        assertEquals("n4", idElements.get(1).getName());
        assertEquals("ids__125", idElements.get(1).getElementId());
        assertEquals(109, idElements.get(1).getStartBytePos());
        assertEquals(135, idElements.get(1).getEndBytePos());
    }


    @Test
    void parse_finds_single_tag_elements() throws RepoException {
        //                     5    5    6          7          8
        //                     4    567890123456 789012345 6789012
        var xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"/></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals("n2", idElements.get(0).getName());
        assertEquals("ids__123", idElements.get(0).getElementId());
        assertEquals(59, idElements.get(0).getStartBytePos());
        assertEquals(77, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_elements_after_single_tag_nodes() throws RepoException {
        //                     5    5    6          7          8          9         10
        //                     4    56789012345678 90123 4567890123 456789012 345678901234567
        var xmlText = XML_HEADER + "<n1><n3 name=\"self\"/><n2 id=\"ids__123\">text</n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals("n2", idElements.get(0).getName());
        assertEquals("ids__123", idElements.get(0).getElementId());
        assertEquals(76, idElements.get(0).getStartBytePos());
        assertEquals(102, idElements.get(0).getEndBytePos());
    }

    @Test
    void parse_finds_elements_after_comments() throws RepoException {
        //                     5    5    6         7          8         9         10        11         12         13        14
        //                     4    56789012345678901234567 890123456 78901234567890123456789012345 678901234 5678901234567890123
        var xmlText = XML_HEADER + "<n1><!-- c1 --><n2 id=\"ids__123\">text</n2><!-- c2 --><n2 id=\"ids__124\">text</n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(2, idElements.size());
        assertEquals("n2", idElements.get(0).getName());
        assertEquals("ids__123", idElements.get(0).getElementId());
        assertEquals(70, idElements.get(0).getStartBytePos());
        assertEquals(96, idElements.get(0).getEndBytePos());
        assertEquals("n2", idElements.get(1).getName());
        assertEquals("ids__124", idElements.get(1).getElementId());
        assertEquals(108, idElements.get(1).getStartBytePos());
        assertEquals(134, idElements.get(1).getEndBytePos());
    }


    @Test
    void parse_finds_elements_with_comments_inside() throws RepoException {
        //                     5    5    6          7          8         9        10        11        12
        //                     4    567890123456 789012345 6789012345678901234567890123456789012345678901
        var xmlText = XML_HEADER + "<n1><n2 id=\"ids__123\"><!-- c1 --><n3>text</n3></n2></n1>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idElements = parser.getIdElementPositions();

        assertEquals(1, idElements.size());
        assertEquals("n2", idElements.get(0).getName());
        assertEquals("ids__123", idElements.get(0).getElementId());
        assertEquals(59, idElements.get(0).getStartBytePos());
        assertEquals(105, idElements.get(0).getEndBytePos());
    }


    @Test
    void parse_finds_multiple_idrefs_in_same_inner_text() throws RepoException {
        //                     5    5    6         7         8         9        10        11        12
        //                     4    5678901234567890123456789012345678901234567890123456789012345678901
        var xmlText = XML_HEADER + "<n1>ids__111 ids__222 ids__333</n1><n2>ids__444 ids__555</n2>";
        var parser = this.createParser(xmlText);

        parser.parse();
        var idRefs = parser.getIdRefPositions();

        assertEquals(5, idRefs.size());
        assertEquals("ids__111", idRefs.get(0).getIdRef());
        assertEquals(59, idRefs.get(0).getStartBytePos());
        assertEquals("ids__222", idRefs.get(1).getIdRef());
        assertEquals(68, idRefs.get(1).getStartBytePos());
        assertEquals("ids__333", idRefs.get(2).getIdRef());
        assertEquals(77, idRefs.get(2).getStartBytePos());
        assertEquals("ids__444", idRefs.get(3).getIdRef());
        assertEquals(94, idRefs.get(3).getStartBytePos());
        assertEquals("ids__555", idRefs.get(4).getIdRef());
        assertEquals(103, idRefs.get(4).getStartBytePos());
    }
}
