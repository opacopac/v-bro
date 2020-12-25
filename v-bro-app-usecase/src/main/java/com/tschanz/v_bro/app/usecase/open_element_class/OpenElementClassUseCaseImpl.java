package com.tschanz.v_bro.app.usecase.open_element_class;

import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListPresenter;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationRequest;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationUseCase;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequest;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsRequestItem;
import com.tschanz.v_bro.app.usecase.select_denominations.SelectDenominationsUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;


@Log
@RequiredArgsConstructor
public class OpenElementClassUseCaseImpl implements OpenElementClassUseCase {
    private final MainState mainState;
    private final ElementClassListPresenter elementClassListPresenter;
    private final ReadDenominationUseCase readDenominationUc;
    private final SelectDenominationsUseCase selectDenominationsUc;
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUseCase;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(OpenElementClassRequest request) {
        var elementClassName = request.getElementClassName();
        var oldElementClassList = this.mainState.getElementClassState().getElementClasses();

        if (elementClassName != null) {
            var msgStart = String.format("UC: opening element class '%s'...", elementClassName);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false, true);
            this.statusPresenter.present(statusResponse1);

            var selectedElementClass = oldElementClassList.getItems()
                .stream()
                .filter(ec -> ec.getName().equals(elementClassName))
                .findFirst()
                .orElse(null);
            var newElementClassList = new SelectedList<>(oldElementClassList.getItems(), selectedElementClass);
            this.mainState.getElementClassState().setElementClasses(newElementClassList);

            var response = ElementClassListResponse.fromDomain(newElementClassList);
            this.elementClassListPresenter.present(response);
        } else {
            log.info("UC clearing element classes");

            var newElementClasses = new SelectedList<>(oldElementClassList.getItems(), null);
            this.mainState.getElementClassState().setElementClasses(newElementClasses);
        }

        var readDenominationRequest = new ReadDenominationRequest();
        this.readDenominationUc.execute(readDenominationRequest);

        var denominations = this.mainState.getDenominationState().getDenominations().getItems();
        List<SelectDenominationsRequestItem> selectDenomination = denominations.size() > 0
            ? List.of(new SelectDenominationsRequestItem(denominations.get(0).getPath(), denominations.get(0).getName()))
            : Collections.emptyList();
        var selectDenominationRequest = new SelectDenominationsRequest(selectDenomination);
        this.selectDenominationsUc.execute(selectDenominationRequest);

        if (request.isAutoOpenFirstElement()) {
            var queryElementRequest = new QueryElementsRequest("");
            this.queryElementsUc.execute(queryElementRequest);

            var elements = this.mainState.getElementState().getQueryResult();
            var elementId = elements.size() > 0
                ? elements.get(0).getId()
                : null;

            var openElementRequest = new OpenElementRequest(elementId, true);
            this.openElementUseCase.execute(openElementRequest);
        }
    }
}
