package com.tschanz.v_bro.app.usecase.select_denominations;

import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class SelectDenominationsUseCaseImpl implements SelectDenominationsUseCase {
    private final MainState mainState;
    private final DenominationsPresenter denominationsPresenter;
    private final QueryElementsUseCase queryElementsUc;


    @Override
    public void execute(SelectDenominationsRequest request) {
        var selectedDenominationNames = Objects.requireNonNull(request.getSelectedDenominationNames());

        log.info(String.format("UC: selecting denomination(s) '%s'...", String.join("', '", selectedDenominationNames)));

        var oldDenominations = this.mainState.getDenominationState().getDenominations();
        var selectedDenominations = oldDenominations.getItems()
            .stream()
            .filter(d -> selectedDenominationNames.contains(d.getName()))
            .collect(Collectors.toList());
        var newDenominations = new MultiSelectedList<>(oldDenominations.getItems(), selectedDenominations);
        this.mainState.getDenominationState().setDenominations(newDenominations);

        var denominationListResponse = DenominationListResponse.fromDomain(newDenominations);
        this.denominationsPresenter.present(denominationListResponse);

        var queryElementsRequest = new QueryElementsRequest(this.mainState.getElementState().getQuery(), true);
        this.queryElementsUc.execute(queryElementsRequest);
    }
}
