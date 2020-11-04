package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.usecase.common.responsemodel.DenominationResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementResponse;
import com.tschanz.v_bro.app.usecase.select_element_class.SelectElementClassPresenter;
import com.tschanz.v_bro.app.usecase.select_element_class.responsemodel.SelectElementClassResponse;
import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.DenominationItem;
import com.tschanz.v_bro.app.presentation.viewmodel.ElementItem;

import java.util.List;
import java.util.stream.Collectors;


public class SelectElementClassPresenterImpl implements SelectElementClassPresenter {
    private final MainModel mainModel;


    public SelectElementClassPresenterImpl(MainModel mainModel) {
        this.mainModel = mainModel;
    }


    @Override
    public void present(SelectElementClassResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }

        if (!response.isError) {
            this.mainModel.elementDenominations.next(this.getDenominationItems(response.denominations));
            this.mainModel.elements.next(this.getElementItems(response.elements));
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }


    private List<DenominationItem> getDenominationItems(List<DenominationResponse> denominations) {
        return denominations
            .stream()
            .map(denomination -> new DenominationItem(denomination.name))
            .collect(Collectors.toList());
    }


    private List<ElementItem> getElementItems(List<ElementResponse> elements) {
        return elements
            .stream()
            .map(element -> new ElementItem(element.id, element.name))
            .collect(Collectors.toList());
    }
}
