package com.tschanz.v_bro.app.usecase.open_element;

import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class OpenElementUseCaseImpl implements OpenElementUseCase {
    private final MainState mainState;
    private final ElementPresenter elementPresenter;
    private final QueryElementsUseCase queryElementsUc;
    private final ReadVersionsUseCase readVersionsUc;
    private final OpenVersionUseCase openVersionUc;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(OpenElementRequest request) {
        var elementId = request.getElementId();

        if (elementId != null) {
            var msgStart = String.format("UC: opening element id '%s'...", elementId);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false, true);
            this.statusPresenter.present(statusResponse1);

            var queryRequest = new QueryElementsRequest(elementId);
            this.queryElementsUc.execute(queryRequest);

            var elements = this.mainState.getElementState().getQueryResult();
            var element = elements
                .stream()
                .filter(e -> e.getId().equals(elementId))
                .findFirst()
                .orElse(null);

            this.mainState.getElementState().setCurrentElement(element);
        } else {
            log.info("UC: clearing selected element");

            this.mainState.getElementState().setCurrentElement(null);
        }

        var currentElement = this.mainState.getElementState().getCurrentElement();
        var elementResponse = ElementResponse.fromDomain(currentElement);
        this.elementPresenter.present(elementResponse);

        var readVersionRequest = new ReadVersionsRequest(elementId);
        this.readVersionsUc.execute(readVersionRequest);

        if (request.isAutoOpenLastVersion()) {
            var versions = this.mainState.getVersionState().getVersions().getItems();
            var versionId = versions.size() > 0
                ? versions.get(versions.size() - 1).getId()
                : null;
            var openVersionRequest = new OpenVersionRequest(versionId);
            this.openVersionUc.execute(openVersionRequest);
        }
    }
}
