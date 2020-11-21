package com.tschanz.v_bro.structure.persistence.xml.stax_parser;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.structure.domain.model.VersionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;

import java.io.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class VersionParserTest {
    private VersionParser parser;


    @BeforeEach
    void setUp() {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        this.parser = new VersionParser(xmlInputFactory);
    }


    @Test
    void readVersions_reads_single_versioned_entry() throws RepoException {
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

        List<VersionData> versionList = this.parser.readVersions(
            stream,
            "gattung",
            "ids__2433574"
        );

        assertEquals(1, versionList.size());
        assertEquals("idd__245406", versionList.get(0).getId());
        assertEquals(LocalDate.parse("2018-12-09"), versionList.get(0).getGueltigVon());
        assertEquals(LocalDate.parse("9999-12-31"), versionList.get(0).getGueltigBis());
    }


    @Test
    void readVersions_reads_multiple_versioned_entries() throws RepoException {
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
            "    <version gueltigBis=\"2020-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7724792549\">\n" +
            "     <name>743126</name>\n" +
            "     <abkuerzung>743126</abkuerzung>\n" +
            "    </version>\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2021-01-01\" id=\"ids__7724792685\">\n" +
            "     <name>743126</name>\n" +
            "     <abkuerzung>743126</abkuerzung>\n" +
            "    </version>\n" +
            "   </betreiber>\n" +
            "  </betreibers>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<VersionData> versionList = this.parser.readVersions(
            stream,
            "betreiber",
            "ids__23395"
        );

        assertEquals(2, versionList.size());
        assertEquals("ids__7724792549", versionList.get(0).getId());
        assertEquals(LocalDate.parse("2018-12-09"), versionList.get(0).getGueltigVon());
        assertEquals(LocalDate.parse("2020-12-31"), versionList.get(0).getGueltigBis());
        assertEquals("ids__7724792685", versionList.get(1).getId());
        assertEquals(LocalDate.parse("2021-01-01"), versionList.get(1).getGueltigVon());
        assertEquals(LocalDate.parse("9999-12-31"), versionList.get(1).getGueltigBis());
    }


    @Test
    void readVersions_reads_single_non_versioned_entry() throws RepoException {
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

        List<VersionData> versionList = this.parser.readVersions(
            stream,
            "partner",
            "ids__12004"
        );

        assertEquals(1, versionList.size());
        assertEquals("ids__12004", versionList.get(0).getId());
        assertEquals(VersionData.LOW_DATE, versionList.get(0).getGueltigVon());
        assertEquals(VersionData.HIGH_DATE, versionList.get(0).getGueltigBis());
        assertEquals(VersionData.DEFAULT_PFLEGESTATUS, versionList.get(0).getPflegestatus());
    }


    /*@Test
    void readVersions_finds_fwd_dep_in_versioned_entries() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">\n" +
            " <subsystemNetz>\n" +
            "  <kanten>\n" +
            "   <kante id=\"ids__1634307168\">\n" +
            "    <version gueltigBis=\"2020-12-31\" gueltigVon=\"2018-12-09\" id=\"ids__7949815462\">\n" +
            "     <haltestelle1>ids__1634280237</haltestelle1>\n" +
            "     <haltestelle2>ids__1634280308</haltestelle2>\n" +
            "     <verwaltung>ids__4057249110</verwaltung>\n" +
            "     <verkehrsmittelTyp>BUS</verkehrsmittelTyp>\n" +
            "     <zuschlagspflichtig>false</zuschlagspflichtig>\n" +
            "     <bahnersatz>false</bahnersatz>" +
            "    </version>\n" +
            "    <version gueltigBis=\"9999-12-31\" gueltigVon=\"2021-01-01\" id=\"ids__7949815463\">\n" +
            "     <haltestelle1>ids__6634285237</haltestelle1>\n" +
            "     <haltestelle2>ids__1634280302</haltestelle2>\n" +
            "     <verwaltung>ids__4057249110</verwaltung>\n" +
            "     <verkehrsmittelTyp>BUS</verkehrsmittelTyp>\n" +
            "     <zuschlagspflichtig>false</zuschlagspflichtig>\n" +
            "     <bahnersatz>false</bahnersatz>" +
            "    </version>\n" +
            "   </kante>\n" +
            "  </kanten>\n" +
            " </subsystemNetz>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<VersionInfo> versionList = this.parser.readVersions(
            stream,
            "kante",
            "ids__1634307168"
        );

        assertEquals(2, versionList.size());
        assertEquals("ids__7949815462", versionList.get(0).getId());
        assertEquals(LocalDate.parse("2018-12-09"), versionList.get(0).getGueltigVon());
        assertEquals(LocalDate.parse("2020-12-31"), versionList.get(0).getGueltigBis());
        assertTrue(versionList.get(0).getFwdDepIds().containsAll(List.of("ids__1634280237", "ids__1634280308", "ids__4057249110")));
        assertEquals("ids__7949815462", versionList.get(0).getId());
        assertEquals(LocalDate.parse("2018-12-09"), versionList.get(0).getGueltigVon());
        assertEquals(LocalDate.parse("2020-12-31"), versionList.get(0).getGueltigBis());
        assertTrue(versionList.get(1).getFwdDepIds().containsAll(List.of("ids__6634285237", "ids__1634280302", "ids__4057249110")));
    }


    @Test
    void readVersions_finds_fwd_dep_in_unversioned_entry() throws RepoException {
        String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:datenrelease xmlns:ns2=\"ch.voev.nova.pflege.common.exporter.datenrelease\" xmlns:ns3=\"ch.voev.nova.pflege.common.exporter.datenrelease.tarifcommons\" xmlns:ns4=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns5=\"ch.voev.nova.pflege.common.exporter.datenrelease.dvmodell\" xmlns:ns6=\"ch.voev.nova.pflege.common.exporter.datenrelease.netz\" id=\"D555.0\">\n" +
            " <subsystemFQF>\n" +
            "  <transportunternehmungen>\n" +
            "   <transportunternehmung id=\"ids__14031\">\n" +
            "    <viaKuerzel>NCM</viaKuerzel>\n" +
            "    <sprache>FRANZOESISCH</sprache>\n" +
            "    <partner>ids__12033</partner>\n" +
            "   </transportunternehmung>\n" +
            "  </transportunternehmungen>" +
            " </subsystemFQF>\n" +
            "</ns2:datenrelease>";
        InputStream stream = new ByteArrayInputStream(xmlText.getBytes());

        List<VersionInfo> versionList = this.parser.readVersions(
            stream,
            "transportunternehmung",
            "ids__14031"
        );

        assertEquals(1, versionList.size());
        assertEquals("ids__14031", versionList.get(0).getId());
        assertTrue(versionList.get(0).isUnversioned());
        assertTrue(versionList.get(0).getFwdDepIds().contains("ids__12033"));
    }*/
}
