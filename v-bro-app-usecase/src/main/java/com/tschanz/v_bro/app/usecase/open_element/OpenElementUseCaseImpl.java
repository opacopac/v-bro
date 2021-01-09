package com.tschanz.v_bro.app.usecase.open_element;

import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
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
    private final AppState appState;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final StatusPresenter statusPresenter;
    private final ElementPresenter elementPresenter;
    private final ReadVersionsUseCase readVersionsUc;


    @Override
    public void execute(OpenElementRequest request) {
        var elementId = request.getElementId();
        var repoType = appState.getCurrentRepoType();
        var elementClass = appState.getCurrentElementClass();
        var selectedDenominationFields = this.appState.getElementDenominations().getSelectedItems();

        if (repoType != null && elementClass != null && elementId != null) {
            var msgStart = String.format("UC: opening element id '%s'...", elementId);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false);
            this.statusPresenter.present(statusResponse1);

            if (!this.trySelectElement(elementClass, elementId)) { // check if element is already loaded by previous query
                try {
                    var elementService = this.elementServiceProvider.getService(repoType);
                    var element = elementService.readElement(elementClass, selectedDenominationFields, elementId);
                    this.appState.setQueryResult(List.of(element));
                    this.appState.setCurrentElement(element);
                } catch (RepoException exception) {
                    var message = String.format("error reading element: %s", exception.getMessage());
                    log.severe(message);

                    this.appState.setQueryResult(Collections.emptyList());
                }
            }

            var statusResponse2 = new StatusResponse(msgStart, false);
            this.statusPresenter.present(statusResponse2);
        } else {
            log.info("UC: clearing selected element");

            this.appState.setCurrentElement(null);
        }

        var currentElement = this.appState.getCurrentElement();
        var elementResponse = ElementResponse.fromDomain(currentElement);
        this.elementPresenter.present(elementResponse);

        if (request.isReadVersions()) {
            var readVersionRequest = new ReadVersionsRequest(request.isAutoOpenLastVersion());
            this.readVersionsUc.execute(readVersionRequest);
        }
    }


    private boolean trySelectElement(ElementClass elementClass, String elementId) {
        var element = this.appState.getQueryResult()
            .stream()
            .filter(e -> e.getElementClass().equals(elementClass) && e.getId().equals(elementId))
            .findFirst()
            .orElse(null);

        if (element != null) {
            this.appState.setCurrentElement(element);
            return true;
        } else {
            return false;
        }
    }
}
