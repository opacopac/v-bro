package com.tschanz.v_bro.app.usecase.select_version_filter;

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


    @Override
    public void execute(SelectVersionFilterRequest request) {
        var versionFilter = request.toDomain();

        log.info(String.format("UC: selecting version filter %tF-%tF %s...", versionFilter.getMinGueltigVon(), versionFilter.getMaxGueltigBis(), versionFilter.getMinPflegestatus().name()));

        this.mainState.getVersionFilterState().setVersionFilter(versionFilter);

        var selectedElement = this.mainState.getElementState().getElements().getSelectedItem();
        if (selectedElement != null) {
            var readVersionsRequest = new ReadVersionsRequest(selectedElement.getId(), true);
            this.readVersionsUc.execute(readVersionsRequest);
        }
    }
}
