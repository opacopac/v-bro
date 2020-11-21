package com.tschanz.v_bro.elements.persistence.xml.service;

import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class VersionParserTest {
    private ElementParser parser;


    @BeforeEach
    void setUp() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        this.parser = new ElementParser(xmlInputFactory);
    }


    @Test
    void readElementData_reads_single_element_in_unversioned_entry() throws RepoException {
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
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<ElementData> elementDataList = this.parser.readElements(
            stream,
            "partner",
            List.of("partnerCode", "partnerKuerzel")
        );

        assertEquals(1, elementDataList.size());
        assertEquals("ids__12004", elementDataList.get(0).getId());
        assertEquals(2, elementDataList.get(0).getNameFieldValues().size());
        assertEquals("partnerCode", elementDataList.get(0).getNameFieldValues().get(0).getName());
        assertEquals("11", elementDataList.get(0).getNameFieldValues().get(0).getValue());
        assertEquals("partnerKuerzel", elementDataList.get(0).getNameFieldValues().get(1).getName());
        assertEquals("SBB", elementDataList.get(0).getNameFieldValues().get(1).getValue());
    }


    @Test
    void readElementData_reads_single_element_in_versioned_entry() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">\n" +
            " <subsystemNetz>\n" +
            "  <gattungen>\n" +
            "   <gattung id=\"ids__2433574\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"idd__245406\">\n" +
            "     <gattungsCode>AG</gattungsCode>\n" +
            "     <verkehrsmittelTyp>BAHN</verkehrsmittelTyp>\n" +
            "    </version>\n" +
            "   </gattung>\n" +
            "  </gattungen>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<ElementData> elementDataList = this.parser.readElements(
            stream,
            "gattung",
            List.of("gattungsCode")
        );

        assertEquals(1, elementDataList.size());
        assertEquals("ids__2433574", elementDataList.get(0).getId());
        assertEquals(1, elementDataList.get(0).getNameFieldValues().size());
        assertEquals("gattungsCode", elementDataList.get(0).getNameFieldValues().get(0).getName());
        assertEquals("AG", elementDataList.get(0).getNameFieldValues().get(0).getValue());
    }


    @Test
    void readElementData_find_multiple_elements() throws RepoException {
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
            "   <betreiber id=\"ids__23395\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7724792549\">\n" +
            "     <name>743126</name>\n" +
            "     <abkuerzung>743126</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "   <betreiber id=\"ids__23025\">\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7949737242\">\n" +
            "     <name>sbg034</name>\n" +
            "     <abkuerzung>sbg034</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "  </betreibers>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<ElementData> elementDataList = this.parser.readElements(
            stream,
            "betreiber",
            List.of("name")
        );

        assertEquals(3, elementDataList.size());
        assertEquals("ids__23361", elementDataList.get(0).getId());
        assertEquals(1, elementDataList.get(0).getNameFieldValues().size());
        assertEquals("name", elementDataList.get(0).getNameFieldValues().get(0).getName());
        assertEquals("743029", elementDataList.get(0).getNameFieldValues().get(0).getValue());
        assertEquals("ids__23395", elementDataList.get(1).getId());
        assertEquals(1, elementDataList.get(1).getNameFieldValues().size());
        assertEquals("name", elementDataList.get(1).getNameFieldValues().get(0).getName());
        assertEquals("743126", elementDataList.get(1).getNameFieldValues().get(0).getValue());
        assertEquals("ids__23025", elementDataList.get(2).getId());
        assertEquals(1, elementDataList.get(2).getNameFieldValues().size());
        assertEquals("name", elementDataList.get(2).getNameFieldValues().get(0).getName());
        assertEquals("sbg034", elementDataList.get(2).getNameFieldValues().get(0).getValue());
    }
}
