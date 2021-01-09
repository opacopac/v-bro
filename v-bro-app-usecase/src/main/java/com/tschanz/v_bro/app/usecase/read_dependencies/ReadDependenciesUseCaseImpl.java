package com.tschanz.v_bro.app.usecase.read_dependencies;

import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.data_structure.domain.model.Dependency;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;


@Log
@RequiredArgsConstructor
public class ReadDependenciesUseCaseImpl implements ReadDependenciesUseCase {
    private final static int MAX_DEPENDENCIES = 100;
    private final AppState appState;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final DependencyPresenter dependencyPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDependenciesRequest request) {
        var repoType = appState.getCurrentRepoType();
        var element = appState.getCurrentElement();
        var version = appState.getCurrentVersion();
        var minGueltigVon = appState.getVersionFilter().getTimelineVon();
        var maxGueltigBis = appState.getVersionFilter().getTimelineBis();
        var minPflegestatus = appState.getVersionFilter().getMinPflegestatus();
        var isFwd = appState.isFwdDependencies();
        var fwdBwdText = isFwd ? "FWD" : "BWD";
        var elementClassFilter = appState.getDependencyElementClasses().getSelectedItem();
        var query = appState.getDependencyElementQuery();
        var dependecyDenominations = appState.getDependencyDenominations().getSelectedItems();

        if (repoType != null && ((isFwd && version != null) || (!isFwd && element != null))) {
            try {
                String msgStart;
                if (isFwd) {
                    msgStart = String.format("UC: reading FWD dependencies of version '%s'...", version.getId());
                } else {
                    msgStart = String.format("UC: reading BWD dependencies of element '%s'...", element.getId());
                }
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false);
                this.statusPresenter.present(statusResponse1);

                var dependencyService = this.dependencyServiceProvider.getService(repoType);
                List<Dependency> dependencies;
                if (isFwd) {
                    dependencies = dependencyService.readFwdDependencies(version, minGueltigVon, maxGueltigBis, minPflegestatus, elementClassFilter, dependecyDenominations, query, MAX_DEPENDENCIES);
                } else {
                    dependencies = dependencyService.readBwdDependencies(element, minGueltigVon, maxGueltigBis, minPflegestatus, elementClassFilter, dependecyDenominations, query, MAX_DEPENDENCIES);
                }

                var msgSuccess = String.format("successfully read %d %s dependencies", dependencies.size(), fwdBwdText);
                log.info(msgSuccess);
                var statusResponse2 = new StatusResponse(msgSuccess, false);
                this.statusPresenter.present(statusResponse2);

                var response = DependencyListResponse.fromDomain(dependencies);
                this.dependencyPresenter.present(response);
            } catch (RepoException exception) {
                var message = String.format("error reading %s dependencies: %s", fwdBwdText, exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing dependency list");

            this.appState.setDependencies(Collections.emptyList());

            var response = DependencyListResponse.fromDomain(Collections.emptyList());
            this.dependencyPresenter.present(response);
        }
    }
}
