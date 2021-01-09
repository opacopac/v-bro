package com.tschanz.v_bro.app.usecase.select_version_aggregate_history;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryPresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.common.types.Triple;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectVersionAggregateHistoryUseCaseImpl implements SelectVersionAggregateHistoryUseCase {
    private final AppState appState;
    private final OpenElementClassUseCase openElementClassUc;
    private final OpenElementUseCase openElementUc;
    private final OpenVersionUseCase openVersionUc;
    private final StatusPresenter statusPresenter;
    private final VersionAggregateHistoryPresenter versionAggregateHistoryPresenter;


    @Override
    public void execute(SelectVersionAggregateHistoryRequest request) {
        var classElementVersion = this.advanceHistoryPointer(request.isGoFwd());
        if (classElementVersion != null) {
            var elementClassName = classElementVersion.first.getName();
            var elementId = classElementVersion.second.getId();
            var versionId = classElementVersion.third.getId();

            var msgStart = String.format("UC: selecting element class '%s' element id '%s' version id '%s' from history...", elementClassName, elementId, versionId);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false);
            this.statusPresenter.present(statusResponse1);

            var versionAggregateHistoryResponse = new VersionAggregateHistoryResponse(this.appState.hasPreviousHistory(), this.appState.hasNextHistory());
            this.versionAggregateHistoryPresenter.present(versionAggregateHistoryResponse);

            var openElementClassRequest = new OpenElementClassRequest(elementClassName, false);
            this.openElementClassUc.execute(openElementClassRequest);

            var openElementRequest = new OpenElementRequest(elementId, true, false);
            this.openElementUc.execute(openElementRequest);

            var openVersionRequest = new OpenVersionRequest(versionId, false);
            this.openVersionUc.execute(openVersionRequest);
        }
    }


    private Triple<ElementClass, ElementData, VersionData> advanceHistoryPointer(boolean isFwd) {
        if (isFwd && this.appState.hasNextHistory()) {
            this.appState.setHistoryPointer(this.appState.getHistoryPointer() + 1);
        } else if (this.appState.hasPreviousHistory()) {
            this.appState.setHistoryPointer(this.appState.getHistoryPointer() - 1);
        }

        return this.appState.getCurrentHistoryEntry();
    }
}
