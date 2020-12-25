package com.tschanz.v_bro.app.usecase.read_versions;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class ReadVersionsUseCaseImpl implements ReadVersionsUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final StatusPresenter statusPresenter;
    private final VersionTimelinePresenter versionTimelinePresenter;


    @Override
    public void execute(ReadVersionsRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var element = mainState.getElementState().getCurrentElement();

        if (repoType != null && element != null) {
            try {
                var msgStart = String.format("UC: reading versions of element id '%s'...", element.getId());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                var versionService = this.versionServiceProvider.getService(repoType);
                var versions = versionService.readVersions(element);

                var versionList = new SelectedList<>(versions, null);
                this.mainState.getVersionState().setVersions(versionList);

                var message = String.format("successfully read %d versions.", versions.size());
                log.info(message);
                var statusResponse = new StatusResponse(message, false, false);
                this.statusPresenter.present(statusResponse);

                var versionTimelineResponse = VersionTimelineResponse.fromDomain(versionList);
                this.versionTimelinePresenter.present(versionTimelineResponse);
            } catch (RepoException exception) {
                var message = String.format("error reading versions: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing versions");

            SelectedList<VersionData> versionList = SelectedList.createEmpty();
            this.mainState.getVersionState().setVersions(versionList);

            var response = VersionTimelineResponse.fromDomain(versionList);
            this.versionTimelinePresenter.present(response);
        }
    }
}
