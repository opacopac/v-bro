package com.tschanz.v_bro.version_aggregates.persistence.jdbc.service;

import com.tschanz.v_bro.common.testing.SpyHelper;
import com.tschanz.v_bro.repo.domain.model.RepoException;
//import com.tschanz.v_bro.repo.persistence.SpyRepoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JdbcVersionAggregateServiceTest {
    private SpyHelper<RepoException> spyHelper = new SpyHelper<>();
    //private SpyRepoService spyRepoService = new SpyRepoService();
    private JdbcVersionAggregateService service;


    @BeforeEach
    void setUp() {
        //this.service = new JdbcVersionAggregateService(spyRepoService, );
    }


    // region findTablesNamesBySuffix

    @Test
    void findTablesNamesBySuffix_happy_day() throws RepoException {
        /*this.mockDatabaseMetaData.mockResultSets.add(new MockResultSet(
            MockResult.fromStrings(new String[][] {{ "TABLE_NAME", "TABLE1" }}),
            MockResult.fromStrings(new String[][] {{ "TABLE_NAME", "TABLE2" }}),
            MockResult.fromStrings(new String[][] {{ "TABLE_NAME", "TABLE3" }})
        ));

        List<String> tableNames = this.jdbcRepoMetadata.findElementClasses("TAB");

        assertEquals(3, tableNames.size());
        assertEquals("TABLE1", tableNames.get(0));
        assertEquals("TABLE2", tableNames.get(1));
        assertEquals("TABLE3", tableNames.get(2));*/
    }
}
