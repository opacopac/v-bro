package com.tschanz.v_bro.app.usecase.select_denominations;

import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Log
@RequiredArgsConstructor
public class SelectDenominationsUseCaseImpl implements SelectDenominationsUseCase {
    private final MainState mainState;
    private final DenominationsPresenter denominationsPresenter;
    private final OpenElementUseCase openElementUc;


    @Override
    public void execute(SelectDenominationsRequest request) {
        var selectedDenominationsReq = Objects.requireNonNull(request.toDomain());
        var selectedDenominationNames = selectedDenominationsReq.stream().map(Denomination::getName).collect(Collectors.toList());

        log.info(String.format("UC: selecting denomination(s) '%s'...", String.join("', '", selectedDenominationNames)));

        var oldDenominations = this.mainState.getDenominationState().getDenominations();
        var selectedDenominations = oldDenominations.getItems()
            .stream()
            .filter(selectedDenominationsReq::contains)
            .collect(Collectors.toList());

        if (selectedDenominations.size() == 0 && oldDenominations.getItems().size() > 0) {
            selectedDenominations = List.of(oldDenominations.getItems().get(0));
        }

        var newDenominations = new MultiSelectedList<>(oldDenominations.getItems(), selectedDenominations);
        this.mainState.getDenominationState().setDenominations(newDenominations);

        var denominationListResponse = DenominationListResponse.fromDomain(newDenominations);
        this.denominationsPresenter.present(denominationListResponse);

        var currentElementId = this.mainState.getElementState().getCurrentElementId();
        if (currentElementId != null) {
            this.mainState.getElementState().setQueryResult(Collections.emptyList());
            var openElementRequest = new OpenElementRequest(currentElementId, false,false);
            this.openElementUc.execute(openElementRequest);
        }
    }
}
