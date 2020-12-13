package com.tschanz.v_bro.app.usecase.open_dependency_version;

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
    private final OpenVersionUseCase openVersionUc;



    @Override
    public void execute(OpenDependencyVersionRequest request) {
        var elementClassName = Objects.requireNonNull(request.getElementClass());
        var elementId = Objects.requireNonNull(request.getElementId());
        var versionId = Objects.requireNonNull(request.getVersionId());

        var openElementClassRequest = new OpenElementClassRequest(elementClassName);
        this.openElementClassUc.execute(openElementClassRequest);

        var queryElementsRequest = new QueryElementsRequest(elementId, true);
        this.queryElementsUc.execute(queryElementsRequest);

        var openVersionRequest = new OpenVersionRequest(versionId);
        this.openVersionUc.execute(openVersionRequest);
    }
}
