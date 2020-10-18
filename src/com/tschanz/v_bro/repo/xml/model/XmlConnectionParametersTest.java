package com.tschanz.v_bro.repo.xml.model;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class XmlConnectionParametersTest {
    private String fileName;
    private XmlConnectionParameters parameters;


    @BeforeEach
    void setUp() {
        this.fileName = "./dr.xml";
        this.parameters = new XmlConnectionParameters(this.fileName);
    }


    @Test
    void test_type_is_always_xml() {
        assertEquals(RepoType.XML, this.parameters.getType());
    }


    @Test
    void test_filename_is_set_when_created() {
        assertEquals(this.fileName, this.parameters.getFilename());
    }
}
