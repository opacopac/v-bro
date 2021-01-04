package com.tschanz.v_bro.app.usecase.open_element;

import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;


@Log
@RequiredArgsConstructor
public class OpenElementUseCaseImpl implements OpenElementUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final ElementPresenter elementPresenter;
    private final ReadVersionsUseCase readVersionsUc;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(OpenElementRequest request) {
        var elementId = request.getElementId();
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var elementClass = mainState.getElementClassState().getCurrentElementClass();
        var selectedDenominationFields = this.mainState.getDenominationState().getCurrentDenominations();

        if (repoType != null && elementId != null) {
            var msgStart = String.format("UC: opening element id '%s'...", elementId);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false, true);
            this.statusPresenter.present(statusResponse1);

            if (!this.mainState.getElementState().trySelectElement(elementClass, elementId)) { // check if element is already loaded by previous query
                try {
                    var elementService = this.elementServiceProvider.getService(repoType);
                    var element = elementService.readElement(elementClass, selectedDenominationFields, elementId);
                    this.mainState.getElementState().setQueryResult(List.of(element));
                    this.mainState.getElementState().setCurrentElement(element);
                } catch (RepoException exception) {
                    var message = String.format("error reading element: %s", exception.getMessage());
                    log.severe(message);

                    this.mainState.getElementState().setQueryResult(Collections.emptyList());
                }
            }
        } else {
            log.info("UC: clearing selected element");

            this.mainState.getElementState().setCurrentElement(null);
        }

        var currentElement = this.mainState.getElementState().getCurrentElement();
        var elementResponse = ElementResponse.fromDomain(currentElement);
        this.elementPresenter.present(elementResponse);

        var readVersionRequest = new ReadVersionsRequest(request.isAutoOpenLastVersion());
        this.readVersionsUc.execute(readVersionRequest);
    }
}
