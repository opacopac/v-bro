package com.tschanz.v_bro.data_structure.persistence.jdbc.service;

import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata.JdbcRepoMetadataServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class JdbcDataStructureServiceTest {
    private JdbcDataStructureService jdbcDataStructureService;


    @BeforeEach
    @SneakyThrows
    void setUp() {
        var jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        jdbcConnectionFactory.openConnection("jdbc:mysql://localhost:3306/test", "", "");
        var jdbcRepoMetadataService = new JdbcRepoMetadataServiceImpl(jdbcConnectionFactory);
        this.jdbcDataStructureService = new JdbcDataStructureService(jdbcRepoMetadataService);
    }


    @Test
    @SneakyThrows
    void readAggregateStructures() {
        var asdf = this.jdbcDataStructureService.getAggregateStructures();

        assertEquals(1, asdf.size());
    }
}
