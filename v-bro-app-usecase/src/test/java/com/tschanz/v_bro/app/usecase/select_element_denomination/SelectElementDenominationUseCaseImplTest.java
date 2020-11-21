package com.tschanz.v_bro.app.usecase.select_element_denomination;

//import com.tschanz.v_bro.structure.domain.service.SpyElementService;


public class SelectElementDenominationUseCaseImplTest {
    /*private SpyElementService spyElementService;
    private SpySelectElementDenominationPresenter spyPresenter;
    private SelectElementDenominationUseCaseImpl selectElementDenominationUc;


    @BeforeEach
    void setUp() {
        this.spyElementService = new SpyElementService();
        RepoServiceProvider repoServiceProvider = new RepoServiceProvider<ElementService>(RepoType.JDBC, this.spyElementService, RepoType.XML, this.spyElementService, RepoType.MOCK, this.spyElementService);
        this.spyPresenter = new SpySelectElementDenominationPresenter();
        this.selectElementDenominationUc = new SelectElementDenominationUseCaseImpl(repoServiceProvider, this.spyPresenter);
    }


    @Test
    void execute__select_2_denominations_returns_combined_element_name() {
        SelectElementDenominationRequest request = new SelectElementDenominationRequest(RepoType.MOCK, "P_PRODUKT_E", List.of("CODE", "BEZEICHNUNG"));
        List<DenominationData> denominations1 = List.of(new DenominationData("CODE", "1"), new DenominationData("BEZEICHNUNG", "eins"), new DenominationData("SORTORDER", "10"));
        List<DenominationData> denominations2 = List.of(new DenominationData("CODE", "2"), new DenominationData("BEZEICHNUNG", "zwei"), new DenominationData("SORTORDER", "20"));
        List<ElementData> elements = List.of(new ElementData("111", denominations1), new ElementData("222", denominations2));
        this.spyElementService.readElementDataResults.add(elements);

        this.selectElementDenominationUc.execute(request);
        SelectElementDenominationResponse response = (SelectElementDenominationResponse) this.spyPresenter.spyHelper.getMethodArgument("present", 0);

        assertNotNull(response);
        assertFalse(response.isError);
        assertEquals(2, response.elements.size());

        assertEquals("111", response.elements.get(0).id);
        assertTrue(response.elements.get(0).name.contains("1"));
        assertTrue(response.elements.get(0).name.contains("eins"));

        assertEquals("222", response.elements.get(1).id);
        assertTrue(response.elements.get(1).name.contains("2"));
        assertTrue(response.elements.get(1).name.contains("zwei"));

        assertEquals(2, response.selectDenominations.size());
        assertEquals("CODE", response.selectDenominations.get(0));
        assertEquals("BEZEICHNUNG", response.selectDenominations.get(1));
    }*/
}
