package com.tschanz.v_bro.app.usecase.read_versions;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class ReadVersionsUseCaseImpl implements ReadVersionsUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final StatusPresenter statusPresenter;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final OpenVersionUseCase openVersionUc;


    @Override
    public void execute(ReadVersionsRequest request) {
        var repoType = Objects.requireNonNull(mainState.getRepoState().getConnectionParameters().getRepoType());
        var elementClass = Objects.requireNonNull(mainState.getElementClassState().getSelectedName());
        var elementId = Objects.requireNonNull(mainState.getElementState().getCurrentElementId());

        try {
            log.info(String.format("UC: reading versions of element class '%s' element id '%s'...", elementClass, elementId));

            var versionService = this.versionServiceProvider.getService(repoType);
            var versions = versionService.readVersionTimeline(elementClass, elementId);
            var slist = new SelectedList<>(versions, null);

            this.mainState.getVersionState().setVersions(slist);

            var message = String.format("successfully read %d versions.", versions.size());
            log.info(message);
            var statusResponse = new StatusResponse(message, false);
            this.statusPresenter.present(statusResponse);

            var versionTimelineResponse = VersionTimelineResponse.fromDomain(slist);
            this.versionTimelinePresenter.present(versionTimelineResponse);

            if (request.isAutoOpenLastVersion() && slist.getItems().size() > 0) {
                var selectVersionId = slist.getItems().get(slist.getItems().size() - 1).getId();
                var readVersionRequest = new OpenVersionRequest(selectVersionId);
                this.openVersionUc.execute(readVersionRequest);
            }
        } catch (RepoException exception) {
            var message = String.format("error reading versions: %s", exception.getMessage());
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }
    }
}
