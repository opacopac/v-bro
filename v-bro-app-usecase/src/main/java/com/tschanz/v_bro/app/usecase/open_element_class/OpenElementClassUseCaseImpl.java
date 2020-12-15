package com.tschanz.v_bro.app.usecase.open_element_class;

import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListPresenter;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationRequest;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class OpenElementClassUseCaseImpl implements OpenElementClassUseCase {
    private final MainState mainState;
    private final ElementClassListPresenter elementClassListPresenter;
    private final ReadDenominationUseCase readDenominationUc;


    @Override
    public void execute(OpenElementClassRequest request) {
        var elementClassName = Objects.requireNonNull(request.getElementClassName());

        log.info(String.format("UC: opening element class '%s'...", elementClassName));

        var oldElementClassList = this.mainState.getElementClassState().getElementClasses();
        var selectedElementClass = oldElementClassList.getItems()
            .stream()
            .filter(ec -> ec.getName().equals(elementClassName))
            .findFirst()
            .orElse(null);
        var newElementClassList = new SelectedList<>(oldElementClassList.getItems(), selectedElementClass);
        this.mainState.getElementClassState().setElementClasses(newElementClassList);

        var response = ElementClassListResponse.fromDomain(newElementClassList);
        this.elementClassListPresenter.present(response);

        if (selectedElementClass != null) {
            var readDenominationRequest = new ReadDenominationRequest(selectedElementClass.getName());
            this.readDenominationUc.execute(readDenominationRequest);
        }
    }
}
