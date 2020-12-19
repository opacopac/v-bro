package com.tschanz.v_bro.app.usecase.select_version_filter;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsRequest;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectVersionFilterUseCaseImpl implements SelectVersionFilterUseCase {
    private final MainState mainState;
    private final ReadVersionsUseCase readVersionsUc;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(SelectVersionFilterRequest request) {
        var versionFilter = request.toDomain();

        var msgStart = String.format("UC: selecting version filter %tF-%tF %s...", versionFilter.getMinGueltigVon(), versionFilter.getMaxGueltigBis(), versionFilter.getMinPflegestatus().name());
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false, true);
        this.statusPresenter.present(statusResponse1);

        this.mainState.getVersionFilterState().setVersionFilter(versionFilter);

        var selectedElementId = this.mainState.getElementState().getCurrentElementId();
        var readVersionsRequest = new ReadVersionsRequest(selectedElementId);
        this.readVersionsUc.execute(readVersionsRequest);
    }
}
