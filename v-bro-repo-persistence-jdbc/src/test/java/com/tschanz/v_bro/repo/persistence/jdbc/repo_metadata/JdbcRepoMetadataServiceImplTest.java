package com.tschanz.v_bro.repo.persistence.jdbc.repo_metadata;

import com.mysql.cj.jdbc.JdbcConnection;
import com.tschanz.v_bro.repo.persistence.jdbc.mock.SpyConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactory;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcConnectionFactoryImpl;
import com.tschanz.v_bro.repo.persistence.jdbc.repo_connection.JdbcServerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


public class JdbcRepoMetadataServiceImplTest {
    private SpyConnectionFactory spyConnectionFactory;
    private JdbcRepoMetadataServiceImpl metadataService;


    @BeforeEach
    void setUp() {
        this.spyConnectionFactory = new SpyConnectionFactory(true);
        this.metadataService = new JdbcRepoMetadataServiceImpl(this.spyConnectionFactory);
    }


    // region findTablesNamesBySuffix

    @Test
    void findTablesNamesBySuffix_happy_day() throws SQLException {
        /*JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactoryImpl();
        this.metadataService = new JdbcRepoMetadataServiceImpl(jdbcConnectionFactory);
        jdbcConnectionFactory.openConnection("jdbc:mysql://localhost:3306/test", "", "");

        this.metadataService.readAllRepoFields();*/


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
