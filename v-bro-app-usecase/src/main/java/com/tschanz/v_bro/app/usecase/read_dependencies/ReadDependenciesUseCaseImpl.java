package com.tschanz.v_bro.app.usecase.read_dependencies;

import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Objects;


@Log
@RequiredArgsConstructor
public class ReadDependenciesUseCaseImpl implements ReadDependenciesUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final DependencyPresenter dependencyPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDependenciesRequest request) {
        var repoType = Objects.requireNonNull(mainState.getRepoState().getConnectionParameters().getRepoType());
        var elementClass = Objects.requireNonNull(mainState.getElementClassState().getSelectedName());
        var elementId = Objects.requireNonNull(mainState.getElementState().getCurrentElement().getId());
        var versionId = Objects.requireNonNull(request.getVersionId());

        try {
            log.info(String.format("UC: reading FWD dependencies of element class '%s' element id '%s' version '%s'...", elementClass, elementId, versionId));

            DependencyService dependencyService = this.dependencyServiceProvider.getService(repoType);
            List<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(elementClass, elementId, versionId);

            String message = "successfully read " + fwdDependencies.size() + " FWD dependencies";
            log.info(message);
            var statusResponse = new StatusResponse(message, false);
            this.statusPresenter.present(statusResponse);

            var response = DependencyListResponse.fromDomain(fwdDependencies);
            this.dependencyPresenter.present(response);
        } catch (RepoException exception) {
            String message = String.format("error reading FWD dependencies: %s", exception.getMessage());
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }
    }
}
