package com.tschanz.v_bro.element_classes.persistence.xml.service;

import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.persistence.xml.model.XmlElementClass;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.versions.persistence.xml.service.MockXmlRepoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ElementClassParserTest {
    private ElementClassParser parser;
    private MockXmlRepoService mockXmlRepoService;


    @BeforeEach
    void setUp() {
        this.parser = new ElementClassParser(SAXParserFactory.newInstance());
        this.mockXmlRepoService = new MockXmlRepoService();
    }


    @Test
    void readElementStructure_detects_element_structures_and_name_fields_in_unversioned_entry() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
        this.mockXmlRepoService.getNewXmlFileStreamResult.add(new ByteArrayInputStream(xmlText.getBytes()));

        ArrayList<XmlElementClass> elementStructure = new ArrayList<>(this.parser.readElementStructure(this.mockXmlRepoService));

        assertEquals(1, elementStructure.size());
        assertEquals("partner", elementStructure.get(0).getName());
        assertEquals(1, elementStructure.get(0).getDataIds().size());
        assertTrue(elementStructure.get(0).getDataIds().contains("ids__12004"));
        assertEquals(3, elementStructure.get(0).getNameFields().size());
        assertTrue(elementStructure.get(0).getNameFields().containsAll(List.of("partnerCode", "partnerKuerzel", "bezeichnung")));
    }


    @Test
    void readElementStructure_detects_element_structures_and_name_fields_in_versioned_entry() throws RepoException {
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
        this.mockXmlRepoService.getNewXmlFileStreamResult.add(new ByteArrayInputStream(xmlText.getBytes()));

        ArrayList<XmlElementClass> elementStructure = new ArrayList<>(this.parser.readElementStructure(this.mockXmlRepoService));

        assertEquals(1, elementStructure.size());
        assertEquals("betreiber", elementStructure.get(0).getName());
        assertEquals(1, elementStructure.get(0).getDataIds().size());
        assertTrue(elementStructure.get(0).getDataIds().contains("ids__23361"));
        assertEquals(2, elementStructure.get(0).getNameFields().size());
        assertTrue(elementStructure.get(0).getNameFields().containsAll(List.of("name", "abkuerzung")));
    }


    @Test
    void readElementStructure_finds_multiple_entries_with_different_names() throws RepoException {
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
        this.mockXmlRepoService.getNewXmlFileStreamResult.add(new ByteArrayInputStream(xmlText.getBytes()));

        ArrayList<XmlElementClass> elementStructure = new ArrayList<>(this.parser.readElementStructure(this.mockXmlRepoService));

        assertEquals(2, elementStructure.size());
        assertEquals("partner", elementStructure.get(0).getName());
        assertEquals(1, elementStructure.get(0).getDataIds().size());
        assertTrue(elementStructure.get(0).getDataIds().contains("ids__12004"));
        assertEquals("betreiber", elementStructure.get(1).getName());
        assertEquals(1, elementStructure.get(1).getDataIds().size());
        assertTrue(elementStructure.get(1).getDataIds().contains("ids__23361"));
    }


    @Test
    void readElementStructure_groups_multiple_same_name_entries() throws RepoException {
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
        this.mockXmlRepoService.getNewXmlFileStreamResult.add(new ByteArrayInputStream(xmlText.getBytes()));

        ArrayList<ElementClass> elementClass = new ArrayList<>(this.parser.readElementStructure(this.mockXmlRepoService));

        assertEquals(1, elementClass.size());
        assertEquals("partner", elementClass.get(0).getName());
        assertEquals(3, elementClass.get(0).getNameFields().size());
        assertTrue(elementClass.get(0).getNameFields().containsAll(List.of("partnerCode", "partnerKuerzel", "bezeichnung")));
    }


    @Test
    void readElementStructure_ignores_nested_ids() throws RepoException {
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
        this.mockXmlRepoService.getNewXmlFileStreamResult.add(new ByteArrayInputStream(xmlText.getBytes()));

        ArrayList<XmlElementClass> elementStructure = new ArrayList<>(this.parser.readElementStructure(this.mockXmlRepoService));

        assertEquals(1, elementStructure.size());
        assertEquals("betreiber", elementStructure.get(0).getName());
        assertEquals(2, elementStructure.get(0).getDataIds().size());
        assertTrue(elementStructure.get(0).getDataIds().contains("ids__23361"));
        assertTrue(elementStructure.get(0).getDataIds().contains("ids__23395"));
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
        this.mockXmlRepoService.getNewXmlFileStreamResult.add(new ByteArrayInputStream(xmlText.getBytes()));

        ArrayList<XmlElementClass> elementStructure = new ArrayList<>(this.parser.readElementStructure(this.mockXmlRepoService));

        assertEquals(2, elementStructure.size());
        assertEquals("tarifnetzKanten", elementStructure.get(1).getName());
        assertEquals(1, elementStructure.get(1).getDataIds().size());
        assertTrue(elementStructure.get(1).getDataIds().contains("idd__9029"));
        assertEquals("verwaltung", elementStructure.get(0).getName());
        assertEquals(1, elementStructure.get(0).getDataIds().size());
        assertTrue(elementStructure.get(0).getDataIds().contains("ids__4057249110"));
    }


    /*@Test
    void test_real_dr() throws RepoException, FileNotFoundException {
        //String fileName = "./dr_examples/mini_dr.xml";
        String fileName = "./dr_examples/stammdaten.xml";
        File xmlFile = new File(fileName);
        InputStream xmlStream = new FileInputStream(xmlFile);

        long start = System.currentTimeMillis();
        ArrayList<ElementStructure> elementStructure = new ArrayList<>(this.parser.readElementStructure(xmlStream));
        long diff = System.currentTimeMillis() - start;
        System.out.println(diff);

        assertEquals(95, elementStructure.size());
    }*/
}
