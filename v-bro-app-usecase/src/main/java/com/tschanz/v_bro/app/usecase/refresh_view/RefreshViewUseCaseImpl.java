package com.tschanz.v_bro.app.usecase.refresh_view;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementRequest;
import com.tschanz.v_bro.app.usecase.open_element.OpenElementUseCase;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionRequest;
import com.tschanz.v_bro.app.usecase.open_version.OpenVersionUseCase;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsRequest;
import com.tschanz.v_bro.app.usecase.query_elements.QueryElementsUseCase;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesRequest;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.app.usecase.read_versions.ReadVersionsUseCase;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class RefreshViewUseCaseImpl implements RefreshViewUseCase {
    private final AppState appState;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceRepoServiceProvider;
    private final ReadElementClassesUseCase readElementClassesUc;
    private final OpenElementClassUseCase openElementClassUc;
    private final QueryElementsUseCase queryElementsUc;
    private final OpenElementUseCase openElementUc;
    private final ReadVersionsUseCase readVersionsUc;
    private final OpenVersionUseCase openVersionUc;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(RefreshViewRequest request) {
        var msgStart = "UC: refreshing view...";
        log.info(msgStart);
        var statusResponse1 = new StatusResponse(msgStart, false);
        this.statusPresenter.present(statusResponse1);

        var repoType = appState.getCurrentRepoType();
        var elementClass = appState.getCurrentElementClass();
        var elementClassName = elementClass != null ? elementClass.getName() : null;
        var element = appState.getCurrentElement();
        var elementId = element != null ? element.getId() : null;
        var version = appState.getCurrentVersion();
        var versionId = version != null ? version.getId() : null;

        // clear caches
        if (repoType != null) {
            try {
                var versionAggregateService = versionAggregateServiceRepoServiceProvider.getService(repoType);
                versionAggregateService.clearCache();
            } catch (RepoException exception) {
                var message = String.format("error clearing caches: %s", exception.getMessage());
                log.severe(message);
            }
        }

        // refresh element classes
        var readElementClassesRequest = new ReadElementClassesRequest();
        this.readElementClassesUc.execute(readElementClassesRequest);

        if (elementClassName != null) {
            var openElementClassRequest = new OpenElementClassRequest(elementClassName, elementId == null);
            this.openElementClassUc.execute(openElementClassRequest);
        }

        // refresh elements
        var queryElementsRequest = new QueryElementsRequest("");
        this.queryElementsUc.execute(queryElementsRequest);

        if (elementId != null) {
            var openElementRequest = new OpenElementRequest(elementId, true, versionId == null);
            this.openElementUc.execute(openElementRequest);
        }

        // refresh version
        if (versionId != null) {
            var openVersionRequest = new OpenVersionRequest(versionId, false);
            this.openVersionUc.execute(openVersionRequest);
        }
    }
}
