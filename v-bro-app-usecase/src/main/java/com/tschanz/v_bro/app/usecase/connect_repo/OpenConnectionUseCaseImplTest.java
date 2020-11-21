package com.tschanz.v_bro.app.usecase.connect_repo;


class OpenConnectionUseCaseImplTest {
    /*private MockRepoService mockRepo;
    private OpenConnectionUseCase openConnectionUc;


    @BeforeEach
    void setUp() {
        this.mockRepo = new MockRepoService();
        this.openConnectionUc = new OpenConnectionUseCaseImpl(Map.of(RepoType.MOCK, this.mockRepo));
    }


    @Test
    void connect_happy_day() throws VBroAppException {
        ConnectionParameters parameters = new JdbcConnectionParameters("XXX", "YYY", "ZZZ");

        this.openConnectionUc.execute(parameters);

        assertEquals(1, this.mockRepo.mockHelper.getMethodCallCount());
        assertTrue(this.mockRepo.mockHelper.isMethodCalled("connect"));
        assertEquals(parameters, this.mockRepo.mockHelper.getMethodArgument("connect", 0));
    }


    @Test
    void connect_sql_exception() {
        this.mockRepo.mockHelper.setThrowException(new RepoException("MEEP", null));
        ConnectionParameters parameters = new JdbcConnectionParameters("XXX", "YYY", "ZZZ");

        Throwable exception = assertThrows(VBroAppException.class, () -> {
            this.openConnectionUc.execute(parameters);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }*/
}
