package com.tschanz.v_bro.app.usecase.select_version_filter;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_filter.VersionFilterPresenter;
import com.tschanz.v_bro.app.presenter.version_filter.VersionFilterResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectVersionFilterUseCaseImpl implements SelectVersionFilterUseCase {
    private final AppState appState;
    private final ReadVersionsUseCase readVersionsUc;
    private final StatusPresenter statusPresenter;
    private final VersionFilterPresenter versionFilterPresenter;


    @Override
    public void execute(SelectVersionFilterRequest request) {
        var versionFilter = request.toDomain();

        var msgStart = String.format("UC: selecting version filter %tF-%tF %s...", versionFilter.getTimelineVon(), versionFilter.getTimelineBis(), versionFilter.getMinPflegestatus().name());
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false);
        this.statusPresenter.present(statusResponse1);

        this.appState.setVersionFilter(versionFilter);

        var versionFilterResponse = VersionFilterResponse.fromDomain(versionFilter);
        this.versionFilterPresenter.present(versionFilterResponse);

        var readVersionsRequest = new ReadVersionsRequest(true);
        this.readVersionsUc.execute(readVersionsRequest);
    }
}
