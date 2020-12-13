package com.tschanz.v_bro.app.usecase.open_element;

import com.tschanz.v_bro.app.presenter.element_list.ElementListPresenter;
import com.tschanz.v_bro.app.presenter.element_list.ElementListResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class OpenElementUseCaseImpl implements OpenElementUseCase {
    private final MainState mainState;
    private final ElementListPresenter elementListPresenter;
    private final ReadVersionsUseCase readVersionsUc;


    @Override
    public void execute(OpenElementRequest request) {
        var requestTimestamp = System.currentTimeMillis();
        var elementId = Objects.requireNonNull(request.getElementId());

        log.info(String.format("UC: opening element id '%s'...", elementId));

        var elements = this.mainState.getElementState().getElements().getItems();
        var selectedElement = elements
            .stream()
            .filter(e -> e.getId().equals(elementId))
            .findFirst()
            .orElse(null);
        var newElements = new SelectedList<>(elements, selectedElement);
        this.mainState.getElementState().setElements(newElements, requestTimestamp);

        var response = ElementListResponse.fromDomain(newElements);
        this.elementListPresenter.present(response);

        var readVersionRequest = new ReadVersionsRequest(elementId, true);
        this.readVersionsUc.execute(readVersionRequest);
    }
}
