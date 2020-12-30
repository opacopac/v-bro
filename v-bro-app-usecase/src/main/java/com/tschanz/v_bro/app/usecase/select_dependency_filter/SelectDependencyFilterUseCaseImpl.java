package com.tschanz.v_bro.app.usecase.select_dependency_filter;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectDependencyFilterUseCaseImpl implements SelectDependencyFilterUseCase {
    private final MainState mainState;
    private final ReadDependenciesUseCase readDependenciesUc;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(SelectDependencyFilterRequest request) {
        var fwdBwdText = request.isFwd() ? "FWD" : "BWD";

        var msgStart = String.format("UC: selecting %s dependency filter...", fwdBwdText);
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false, true);
        this.statusPresenter.present(statusResponse1);

        this.mainState.getDependencyState().setFwdDependencies(request.isFwd());

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }
}
