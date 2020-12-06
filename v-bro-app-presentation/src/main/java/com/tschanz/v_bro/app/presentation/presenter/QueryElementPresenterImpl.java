package com.tschanz.v_bro.app.presentation.presenter;

import com.tschanz.v_bro.app.presentation.viewmodel.ErrorStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.InfoStatusItem;
import com.tschanz.v_bro.app.presentation.viewmodel.MainModel;
import com.tschanz.v_bro.app.presentation.viewmodel.converter.ElementItemConverter;
import com.tschanz.v_bro.app.usecase.query_element.QueryElementPresenter;
import com.tschanz.v_bro.app.usecase.query_element.responsemodel.QueryElementResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class QueryElementPresenterImpl implements QueryElementPresenter {
    private final MainModel mainModel;


    @Override
    public void present(@NonNull QueryElementResponse response) {
        if (!response.isError && this.mainModel.lastElementQueryTimestamp < response.requestTimestamp) {
            this.mainModel.lastElementQueryTimestamp = response.requestTimestamp;
            this.mainModel.elements.next(ElementItemConverter.fromResponse(response.elements, null)); // TODO
            this.mainModel.appStatus.next(new InfoStatusItem(response.message));
        } else {
            this.mainModel.appStatus.next(new ErrorStatusItem(response.message));
        }
    }
}
