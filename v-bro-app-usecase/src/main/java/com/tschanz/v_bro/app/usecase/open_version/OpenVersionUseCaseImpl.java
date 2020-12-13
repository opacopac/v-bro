package com.tschanz.v_bro.app.usecase.open_version;

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

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class OpenVersionUseCaseImpl implements OpenVersionUseCase {
    private final MainState mainState;
    private final ReadVersionAggregateUseCase readVersionAggregateUc;
    private final ReadDependenciesUseCase readDependenciesUc;
    private final VersionTimelinePresenter versionTimelinePresenter;


    @Override
    public void execute(OpenVersionRequest request) {
        var versionId = Objects.requireNonNull(request.getVersionId());

        log.info(String.format("UC: opening version id '%s'...", versionId));

        var oldVersions = this.mainState.getVersionState().getVersions().getItems();
        var selectedVersion = oldVersions
            .stream()
            .filter(v -> v.getId().equals(versionId))
            .findFirst()
            .orElse(null);
        var newSelectedVersions = new SelectedList<>(oldVersions, selectedVersion);
        this.mainState.getVersionState().setVersions(newSelectedVersions);

        var versionTimelineResponse = VersionTimelineResponse.fromDomain(newSelectedVersions);
        this.versionTimelinePresenter.present(versionTimelineResponse);

        if (newSelectedVersions.getSelectedItem() != null) {
            var selectVersionId = newSelectedVersions.getSelectedItem().getId();

            var readVersionAggregateRequest = new ReadVersionAggregateRequest(selectVersionId);
            this.readVersionAggregateUc.execute(readVersionAggregateRequest);

            var readDependenciesRequest = new ReadDependenciesRequest(selectVersionId);
            this.readDependenciesUc.execute(readDependenciesRequest);
        }
    }
}
