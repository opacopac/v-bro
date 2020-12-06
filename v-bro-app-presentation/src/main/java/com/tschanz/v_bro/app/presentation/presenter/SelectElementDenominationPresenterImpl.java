package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.*;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.ElementItemConverter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationPresenter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SelectElementDenominationPresenterImpl implements SelectElementDenominationPresenter {
    private final MainModel mainModel;


    @Override
    public void present(@NonNull SelectElementDenominationResponse response) {
        if (!response.isError) {
            this.mainModel.elements.next(ElementItemConverter.fromResponse(response.elements, this.getSelectedElementId()));
            var oldDenominations = this.mainModel.elementDenominations.getCurrentValue();
            var newDenominations = new MultiSelectableItemList<DenominationItem>(oldDenominations.getItems(), response.selectDenominations);
            this.mainModel.elementDenominations.next(newDenominations);
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }


    private String getSelectedElementId() {
        if (this.mainModel.elements.getCurrentValue() != null && this.mainModel.elements.getCurrentValue().getSelectedItem() != null) {
            return this.mainModel.elements.getCurrentValue().getSelectedItem().getId();
        } else {
            return null;
        }
    }
}
