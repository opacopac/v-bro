package com.tschanz.v_bro.repo.usecase.close_connection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.persistence.mock.service.MockRepoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class CloseConnectionUseCaseImplTest {
    private MockRepoService mockRepo;
    private CloseConnectionUseCase closeConnectionUc;


    @BeforeEach
    void setUp() {
        this.mockRepo = new MockRepoService();
        this.closeConnectionUc = new CloseConnectionUseCaseImpl(Map.of(RepoType.MOCK, this.mockRepo));
    }


    @Test
    void connect_happy_day() throws VBroAppException {
        this.closeConnectionUc.disconnect();

        assertEquals(1, mockRepo.mockHelper.getMethodCallCount());
        assertTrue(mockRepo.mockHelper.isMethodCalled("disconnect"));
    }


    @Test
    void connect_sql_exception() {
        this.mockRepo.mockHelper.setThrowException(new RepoException("MEEP", null));

        Throwable exception = assertThrows(VBroAppException.class, () -> {
            this.closeConnectionUc.disconnect();
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }
}
