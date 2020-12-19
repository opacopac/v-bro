package com.tschanz.v_bro.app.usecase.open_dependency_version;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class OpenDependencyVersionUseCaseImpl implements OpenDependencyVersionUseCase {
    private final OpenElementClassUseCase openElementClassUc;
    private final OpenElementUseCase openElementUc;
    private final OpenVersionUseCase openVersionUc;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(OpenDependencyVersionRequest request) {
        var elementClassName = Objects.requireNonNull(request.getElementClass());
        var elementId = Objects.requireNonNull(request.getElementId());
        var versionId = Objects.requireNonNull(request.getVersionId());

        var msgStart =  String.format("UC: opening dependency version of element class '%s' element id '%s' version id '%s'...", elementClassName, elementId, versionId);
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false, true);
        this.statusPresenter.present(statusResponse1);

        var openElementClassRequest = new OpenElementClassRequest(elementClassName, false);
        this.openElementClassUc.execute(openElementClassRequest);

        var openElementRequest = new OpenElementRequest(elementId, false);
        this.openElementUc.execute(openElementRequest);

        var openVersionRequest = new OpenVersionRequest(versionId);
        this.openVersionUc.execute(openVersionRequest);
    }
}
