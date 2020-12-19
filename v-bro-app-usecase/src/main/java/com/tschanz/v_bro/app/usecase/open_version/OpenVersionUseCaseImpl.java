package com.tschanz.v_bro.app.usecase.open_version;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import com.tschanz.v_bro.app.usecase.read_version_aggregate.ReadVersionAggregateRequest;
import com.tschanz.v_bro.app.usecase.read_version_aggregate.ReadVersionAggregateUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class OpenVersionUseCaseImpl implements OpenVersionUseCase {
    private final MainState mainState;
    private final ReadVersionAggregateUseCase readVersionAggregateUc;
    private final ReadDependenciesUseCase readDependenciesUc;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(OpenVersionRequest request) {
        var versionId = request.getVersionId();
        var oldVersionList = this.mainState.getVersionState().getVersions().getItems();

        if (versionId != null) {
            var msgStart = String.format("UC: opening version id '%s'...", versionId);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false, true);
            this.statusPresenter.present(statusResponse1);

            var selectedVersion = oldVersionList
                .stream()
                .filter(v -> v.getId().equals(versionId))
                .findFirst()
                .orElse(null);
            var versionList = new SelectedList<>(oldVersionList, selectedVersion);
            this.mainState.getVersionState().setVersions(versionList);
        } else {
            log.info("UC: clearing selected version...");

            var versionList = new SelectedList<>(oldVersionList, null);
            this.mainState.getVersionState().setVersions(versionList);
        }

        var versionTimelineResponse = VersionTimelineResponse.fromDomain(this.mainState.getVersionState().getVersions());
        this.versionTimelinePresenter.present(versionTimelineResponse);

        var readVersionAggregateRequest = new ReadVersionAggregateRequest();
        this.readVersionAggregateUc.execute(readVersionAggregateRequest);

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }
}
