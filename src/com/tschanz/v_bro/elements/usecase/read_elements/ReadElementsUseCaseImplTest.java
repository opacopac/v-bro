package com.tschanz.v_bro.elements.usecase.read_elements;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.mock.service.MockElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class ReadElementsUseCaseImplTest {
    private MockElementService mockElementClassService;
    private ReadElementsUseCaseImpl readElementsUc;
    private OpenConnectionResponse.RepoConnection repoConnection;


    @BeforeEach
    void setUp() {
        this.mockElementClassService = new MockElementService();
        this.readElementsUc = new ReadElementsUseCaseImpl(Map.of(RepoType.MOCK, this.mockElementClassService));
        this.repoConnection = new OpenConnectionResponse.RepoConnection(RepoType.MOCK);
    }


    @Test
    void readElements_happy_day() throws VBroAppException {
        ElementData element1 = new ElementData("ELEMENT1");
        ElementData element2 = new ElementData("ELEMENT2");
        this.mockElementClassService.readElementDataResults.add(Arrays.asList(element1, element2));

        ReadElementsResponse response = this.readElementsUc.readElements(this.repoConnection, "TABLE1_E", Collections.emptyList()); // TODO: name fields

        assertEquals(2, response.elements.size());
        assertEquals("ELEMENT1", response.elements.get(0).name);
        assertEquals("ELEMENT2", response.elements.get(1).name);
    }


    @Test
    void readElements_exception_reading_elements() {
        this.mockElementClassService.mockHelper.setThrowException(new RepoException("MEEP", null));

        Throwable exception = assertThrows(VBroAppException.class, () -> {
            this.readElementsUc.readElements(this.repoConnection, "TABLE1_E", Collections.emptyList()); // TODO: name fields
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }
}
