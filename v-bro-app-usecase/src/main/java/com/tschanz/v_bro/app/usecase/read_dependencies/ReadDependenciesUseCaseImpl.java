package com.tschanz.v_bro.app.usecase.read_dependencies;

import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;


@Log
@RequiredArgsConstructor
public class ReadDependenciesUseCaseImpl implements ReadDependenciesUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final DependencyPresenter dependencyPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDependenciesRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var version = mainState.getVersionState().getCurrentVersion();

        if (repoType != null && version != null) {
            try {
                var msgStart =  String.format("UC: reading FWD dependencies of version '%s'...", version.getId());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                DependencyService dependencyService = this.dependencyServiceProvider.getService(repoType);
                List<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(version);

                String msgSuccess = String.format("successfully read %d FWD dependencies", fwdDependencies.size());
                log.info(msgSuccess);
                var statusResponse2 = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse2);

                var response = DependencyListResponse.fromDomain(fwdDependencies);
                this.dependencyPresenter.present(response);
            } catch (RepoException exception) {
                String message = String.format("error reading FWD dependencies: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing dependency list");

            this.mainState.getDependencyState().setFwdDependencies(Collections.emptyList());

            var response = DependencyListResponse.fromDomain(Collections.emptyList());
            this.dependencyPresenter.present(response);
        }
    }
}
