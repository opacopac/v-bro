package com.tschanz.v_bro.app.usecase.open_version;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryPresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate_history.VersionAggregateHistoryResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import com.tschanz.v_bro.app.usecase.read_version_aggregate.ReadVersionAggregateRequest;
import com.tschanz.v_bro.app.usecase.read_version_aggregate.ReadVersionAggregateUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.common.types.Triple;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class OpenVersionUseCaseImpl implements OpenVersionUseCase {
    private final AppState appState;
    private final ReadVersionAggregateUseCase readVersionAggregateUc;
    private final ReadDependenciesUseCase readDependenciesUc;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final StatusPresenter statusPresenter;
    private final VersionAggregateHistoryPresenter versionAggregateHistoryPresenter;


    @Override
    public void execute(OpenVersionRequest request) {
        var versionId = request.getVersionId();
        var oldVersionList = this.appState.getVersions().getItems();

        if (versionId != null) {
            var msgStart = String.format("UC: opening version id '%s'...", versionId);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false);
            this.statusPresenter.present(statusResponse1);

            var selectedVersion = oldVersionList
                .stream()
                .filter(v -> v.getId().equals(versionId))
                .findFirst()
                .orElse(null);
            var versionList = new SelectedList<>(oldVersionList, selectedVersion);
            this.appState.setVersions(versionList);

            if (request.isAppendToHistory()) {
                this.appendCurrentStateToHistory();
            }

            var response = new VersionAggregateHistoryResponse(this.appState.hasPreviousHistory(), this.appState.hasNextHistory());
            this.versionAggregateHistoryPresenter.present(response);
        } else {
            log.info("UC: clearing selected version...");

            var versionList = new SelectedList<>(oldVersionList, null);
            this.appState.setVersions(versionList);
        }

        var versionTimelineResponse = VersionTimelineResponse.fromDomain(this.appState.getVersions());
        this.versionTimelinePresenter.present(versionTimelineResponse);

        var readVersionAggregateRequest = new ReadVersionAggregateRequest();
        this.readVersionAggregateUc.execute(readVersionAggregateRequest);

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }


    public void appendCurrentStateToHistory() {
        var elementClass = this.appState.getCurrentElementClass();
        var element = this.appState.getCurrentElement();
        var version = this.appState.getCurrentVersion();
        var lastEntry = this.appState.getCurrentHistoryEntry();
        var lastElementClassName = lastEntry != null ? lastEntry.first.getName() : null;
        var lastElementId = lastEntry != null ? lastEntry.second.getId() : null;
        var lastVersionId = lastEntry != null ? lastEntry.third.getId() : null;

        if (elementClass != null && element != null && version != null) {
            // clear tail if pointer is not at the end
            var itemsToRemove = this.appState.getVersionAggregateHistory().size() - this.appState.getHistoryPointer() - 1;
            while (itemsToRemove > 0) {
                itemsToRemove--;
                this.appState.getVersionAggregateHistory().remove(this.appState.getVersionAggregateHistory().size() - 1);
            }

            if (!(elementClass.getName().equals(lastElementClassName) && element.getId().equals(lastElementId) && version.getId().equals(lastVersionId))) {
                var entry = new Triple<>(elementClass, element, version);
                this.appState.getVersionAggregateHistory().add(entry);
                this.appState.setHistoryPointer(this.appState.getVersionAggregateHistory().size() - 1);
            }
        }
    }
}
