package com.tschanz.v_bro.app.usecase.select_dependency_direction;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesRequest;
import com.tschanz.v_bro.app.usecase.read_dependencies.ReadDependenciesUseCase;
import com.tschanz.v_bro.app.usecase.read_dependency_element_classes.ReadDependencyElementClassesRequest;
import com.tschanz.v_bro.app.usecase.read_dependency_element_classes.ReadDependencyElementClassesUseCase;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_element_class.SelectDependencyElementClassUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class SelectDependencyDirectionUseCaseImpl implements SelectDependencyDirectionUseCase {
    private final AppState appState;
    private final ReadDependenciesUseCase readDependenciesUc;
    private final StatusPresenter statusPresenter;
    private final ReadDependencyElementClassesUseCase readDependencyElementClassesUc;
    private final SelectDependencyElementClassUseCase selectDependencyElementClassUc;


    @Override
    public void execute(SelectDependencyDirectionRequest request) {
        var fwdBwdText = request.isFwd() ? "FWD" : "BWD";

        var msgStart = String.format("UC: selecting %s dependency direction...", fwdBwdText);
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false);
        this.statusPresenter.present(statusResponse1);

        this.appState.setFwdDependencies(request.isFwd());

        var readDependencyStructureRequest = new ReadDependencyElementClassesRequest();
        this.readDependencyElementClassesUc.execute(readDependencyStructureRequest);

        var selectDependencyElementClassRequest = new SelectDependencyElementClassRequest(null);
        this.selectDependencyElementClassUc.execute(selectDependencyElementClassRequest);

        var readDependenciesRequest = new ReadDependenciesRequest();
        this.readDependenciesUc.execute(readDependenciesRequest);
    }
}
