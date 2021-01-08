package com.tschanz.v_bro.app.usecase.open_element_class;

import com.tschanz.v_bro.app.presenter.element_class.ElementClassPresenter;
import com.tschanz.v_bro.app.presenter.element_class.ElementClassResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationRequest;
import com.tschanz.v_bro.app.usecase.read_denominations.ReadDenominationUseCase;
import com.tschanz.v_bro.app.usecase.read_dependency_element_classes.ReadDependencyElementClassesRequest;
import com.tschanz.v_bro.app.usecase.read_dependency_element_classes.ReadDependencyElementClassesUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class OpenElementClassUseCaseImpl implements OpenElementClassUseCase {
    private final AppState appState;
    private final ElementClassPresenter elementClassPresenter;
    private final StatusPresenter statusPresenter;
    private final ReadDenominationUseCase readDenominationUc;
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUseCase;
    private final ReadDependencyElementClassesUseCase readDependencyElementClassesUc;
    private final SelectDependencyElementClassUseCase selectDependencyElementClassUc;


    @Override
    public void execute(OpenElementClassRequest request) {
        var elementClassName = request.getElementClassName();
        var oldElementClassList = this.appState.getElementClasses();

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
            this.appState.setElementClasses(newElementClassList);

            var response = ElementClassResponse.fromDomain(newElementClassList);
            this.elementClassPresenter.present(response);
        } else {
            log.info("UC clearing element classes");

            var newElementClasses = new SelectedList<>(oldElementClassList.getItems(), null);
            this.appState.setElementClasses(newElementClasses);
        }

        var readDenominationRequest = new ReadDenominationRequest();
        this.readDenominationUc.execute(readDenominationRequest);

        if (request.isAutoOpenFirstElement()) {
            var queryElementRequest = new QueryElementsRequest("");
            this.queryElementsUc.execute(queryElementRequest);

            var elements = this.appState.getQueryResult();
            var elementId = elements.size() > 0
                ? elements.get(0).getId()
                : null;

            var openElementRequest = new OpenElementRequest(elementId, true, true);
            this.openElementUseCase.execute(openElementRequest);
        }

        var readDependencyStructureRequest = new ReadDependencyElementClassesRequest();
        this.readDependencyElementClassesUc.execute(readDependencyStructureRequest);

        var selectDependencyElementClassRequest = new SelectDependencyElementClassRequest(null);
        this.selectDependencyElementClassUc.execute(selectDependencyElementClassRequest);
    }
}
