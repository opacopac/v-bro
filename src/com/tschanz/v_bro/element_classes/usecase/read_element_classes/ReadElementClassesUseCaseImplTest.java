package com.tschanz.v_bro.element_classes.usecase.read_element_classes;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.persistence.mock.MockElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class ReadElementClassesUseCaseImplTest {
    private MockElementClassService mockElementClassService;
    private ReadElementClassesUseCaseImpl readElementTablesUc;
    private OpenConnectionResponse.RepoConnection connection;


    @BeforeEach
    void setUp() {
        this.mockElementClassService = new MockElementClassService();
        this.readElementTablesUc = new ReadElementClassesUseCaseImpl(Map.of(RepoType.MOCK, this.mockElementClassService));
        this.connection = new OpenConnectionResponse.RepoConnection(RepoType.MOCK);
    }


    @Test
    void readElementTableNames_happy_day() throws VBroAppException {
        ElementClass elementClass1 = new ElementClass("TABLE1_E");
        ElementClass elementClass2 = new ElementClass("TABLE2_E");
        this.mockElementClassService.readElementClassesResult.add(Arrays.asList(elementClass1, elementClass2));

        List<String> tableNames = this.readElementTablesUc.readElementClasses(this.connection).elementTableNames;

        assertEquals(2, tableNames.size());
        assertEquals(elementClass1.getName(), tableNames.get(0));
        assertEquals(elementClass2.getName(), tableNames.get(1));
    }


    @Test
    void readElementTableNames_exception() {
        this.mockElementClassService.mockHelper.setThrowException(new RepoException("MEEP", null));

        Throwable exception = assertThrows(VBroAppException.class, () -> {
            this.readElementTablesUc.readElementClasses(this.connection);
        });

        assertTrue(exception.getMessage().contains("MEEP"));
    }
}
