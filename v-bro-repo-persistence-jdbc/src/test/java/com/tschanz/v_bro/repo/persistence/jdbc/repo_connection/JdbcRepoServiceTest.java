package com.tschanz.v_bro.repo.persistence.jdbc.repo_connection;

import com.tschanz.v_bro.repo.persistence.jdbc.mock.SpyConnectionFactory;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


class JdbcRepoServiceTest {
    private SpyConnectionFactory spyConnectionFactory;
    private JdbcRepoService jdbcRepo;


    @BeforeEach
    void setUp() {
        this.spyConnectionFactory = new SpyConnectionFactory(false);
        this.jdbcRepo = new JdbcRepoService(spyConnectionFactory);
    }


    // region connect

    @Test
    void connect_happy_day() throws RepoException {
        JdbcConnectionParameters parameters = new JdbcConnectionParameters("XXX", "YYY", "ZZZ");

        this.jdbcRepo.connect(parameters);

        assertNotNull(this.spyConnectionFactory.getCurrentConnection());
    }


    @Test
    void connect_sql_exception() {
        this.spyConnectionFactory.spyHelper.setThrowException(new SQLException("MEEP"));
        JdbcConnectionParameters parameters = new JdbcConnectionParameters("XXX", "YYY", "ZZZ");

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepo.connect(parameters);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion


    // region disconnect

    @Test
    void disconnect_happy_day() throws RepoException {
        this.jdbcRepo.disconnect();

        assertNull(this.spyConnectionFactory.getCurrentConnection());
    }


    @Test
    void disconnect_sql_exception() {
        this.spyConnectionFactory.spyHelper.setThrowException(new SQLException("MEEP"));

        Exception exception = assertThrows(RepoException.class, () -> {
            this.jdbcRepo.disconnect();
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }

    // endregion
}
