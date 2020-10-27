package com.tschanz.v_bro.repo.usecase.open_connection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class OpenConnectionUseCaseImplTest {
    private MockRepoService mockRepo;
    private OpenConnectionUseCase openConnectionUc;


    @BeforeEach
    void setUp() {
        this.mockRepo = new MockRepoService();
        this.openConnectionUc = new OpenConnectionUseCaseImpl(Map.of(RepoType.MOCK, this.mockRepo));
    }


    @Test
    void connect_happy_day() throws VBroAppException {
        ConnectionParameters parameters = new JdbcConnectionParameters("XXX", "YYY", "ZZZ");

        this.openConnectionUc.connect(parameters);

        assertEquals(1, this.mockRepo.mockHelper.getMethodCallCount());
        assertTrue(this.mockRepo.mockHelper.isMethodCalled("connect"));
        assertEquals(parameters, this.mockRepo.mockHelper.getMethodArgument("connect", 0));
    }


    @Test
    void connect_sql_exception() {
        this.mockRepo.mockHelper.setThrowException(new RepoException("MEEP", null));
        ConnectionParameters parameters = new JdbcConnectionParameters("XXX", "YYY", "ZZZ");

        Throwable exception = assertThrows(VBroAppException.class, () -> {
            this.openConnectionUc.connect(parameters);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }
}
