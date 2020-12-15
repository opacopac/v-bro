package com.tschanz.v_bro.app.usecase.open_dependency_version;

import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class OpenDependencyVersionUseCaseImpl implements OpenDependencyVersionUseCase {
    private final OpenElementClassUseCase openElementClassUc;
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUc;
    private final OpenVersionUseCase openVersionUc;



    @Override
    public void execute(OpenDependencyVersionRequest request) {
        var elementClassName = Objects.requireNonNull(request.getElementClass());
        var elementId = Objects.requireNonNull(request.getElementId());
        var versionId = Objects.requireNonNull(request.getVersionId());

        log.info(String.format("UC: opening dependency version of element class '%s' element id '%s' version id '%s'...", elementClassName, elementId, versionId));

        var openElementClassRequest = new OpenElementClassRequest(elementClassName);
        this.openElementClassUc.execute(openElementClassRequest);

        var queryElementsRequest = new QueryElementsRequest(elementId);
        this.queryElementsUc.execute(queryElementsRequest);

        var openElementRequest = new OpenElementRequest(elementId);
        this.openElementUc.execute(openElementRequest);

        var openVersionRequest = new OpenVersionRequest(versionId);
        this.openVersionUc.execute(openVersionRequest);
    }
}
