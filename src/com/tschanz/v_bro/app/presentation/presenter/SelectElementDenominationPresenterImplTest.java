package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import com.tschanz.v_bro.common.reactive.GenericSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SelectElementDenominationPresenterImplTest {
    private boolean isSetup = true;
    private MainModel mainModel;
    private SelectElementDenominationPresenterImpl selectElementDenominationPresenter;


    @BeforeEach
    void setUp() {
        this.mainModel = new MainModel();
        this.selectElementDenominationPresenter = new SelectElementDenominationPresenterImpl(this.mainModel);
    }


    @Test
    void present__2_elements_with_2_selected_denominations__updates_element_list__selects_denominations() {
        List<ElementResponse> elementResponses = List.of(new ElementResponse("111", "1 - eins"), new ElementResponse("222", "2 - zwei"));
        List<String> selectDenominations = List.of("CODE", "BEZEICHNUNG");
        String message = "success";
        SelectElementDenominationResponse response = new SelectElementDenominationResponse(elementResponses, selectDenominations, message, false);
        SelectedItemList<ElementItem> oldElements = new SelectedItemList<>(List.of(new ElementItem("111", "eins"), new ElementItem("222", "zwei")), "222");
        this.mainModel.elements.next(oldElements);
        MultiSelectedItemList<DenominationItem> oldDenominations = new MultiSelectedItemList<>(List.of(new DenominationItem("CODE"), new DenominationItem("BEZEICHNUNG"), new DenominationItem("SORTORDER")), List.of("BEZEICHNUNG"));
        this.mainModel.elementDenominations.next(oldDenominations);

        this.mainModel.elements.subscribe(new GenericSubscriber<>(elements -> {
            if (this.isSetup) {
                return;
            }
            assertEquals(2, elements.getItems().size());
            assertEquals("111", elements.getItems().get(0).getId());
            assertEquals("1 - eins", elements.getItems().get(0).toString());
            assertEquals("222", elements.getItems().get(1).getId());
            assertEquals("2 - zwei", elements.getItems().get(1).toString());
            assertEquals("222", elements.getSelectedItem().getId());
        }));
        this.mainModel.elementDenominations.subscribe(new GenericSubscriber<>(denominations -> {
            if (this.isSetup) {
                return;
            }
            assertEquals(3, denominations.getItems().size());
            assertEquals("CODE", denominations.getItems().get(0).getName());
            assertEquals("BEZEICHNUNG", denominations.getItems().get(1).getName());
            assertEquals("SORTORDER", denominations.getItems().get(2).getName());
            assertEquals(2, denominations.getSelectedItems().size());
            assertEquals("CODE", denominations.getSelectedItems().get(0).getName());
            assertEquals("BEZEICHNUNG", denominations.getSelectedItems().get(1).getName());
        }));
        this.mainModel.appStatus.subscribe(new GenericSubscriber<>(appStatus -> {
            if (this.isSetup) {
                return;
            }
            assertEquals(InfoStatusItem.class, appStatus.getClass());
            assertEquals(message, appStatus.getStatusText());
        }));

        this.isSetup = false;
        this.selectElementDenominationPresenter.present(response);
    }
}
