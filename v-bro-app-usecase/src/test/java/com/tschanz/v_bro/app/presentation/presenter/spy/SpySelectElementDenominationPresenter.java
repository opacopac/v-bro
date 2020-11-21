package com.tschanz.v_bro.app.presentation.presenter.spy;

import com.tschanz.v_bro.app.usecase.select_element_denomination.SelectElementDenominationPresenter;
import com.tschanz.v_bro.app.usecase.select_element_denomination.responsemodel.SelectElementDenominationResponse;
import com.tschanz.v_bro.common.testing.SpyHelper;


public class SpySelectElementDenominationPresenter implements SelectElementDenominationPresenter {
    public SpyHelper<Exception> spyHelper = new SpyHelper<>();


    @Override
    public void present(SelectElementDenominationResponse response) {
        this.spyHelper.reportMethodCall("present", response);
    }
}
