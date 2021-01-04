package com.tschanz.v_bro.app.usecase.read_dependencies;

import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
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
    private final MainState mainState;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final DependencyPresenter dependencyPresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadDependenciesRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var element = mainState.getElementState().getCurrentElement();
        var version = mainState.getVersionState().getCurrentVersion();
        var minGueltigVon = mainState.getVersionFilterState().getVersionFilter().getTimelineVon();
        var maxGueltigBis = mainState.getVersionFilterState().getVersionFilter().getTimelineBis();
        var minPflegestatus = mainState.getVersionFilterState().getVersionFilter().getMinPflegestatus();
        var isFwd = mainState.getDependencyState().isFwdDependencies();
        var fwdBwdText = isFwd ? "FWD" : "BWD";

        if (repoType != null && ((isFwd && version != null) || (!isFwd && element != null))) {
            try {
                var msgStart =  String.format("UC: reading %s dependencies of version '%s'...", fwdBwdText, version.getId());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                var dependencyService = this.dependencyServiceProvider.getService(repoType);
                List<Dependency> dependencies;
                if (isFwd) {
                    dependencies = dependencyService.readFwdDependencies(version, minGueltigVon, maxGueltigBis, minPflegestatus);
                } else {
                    dependencies = dependencyService.readBwdDependencies(element, minGueltigVon, maxGueltigBis, minPflegestatus);
                }

                var msgSuccess = String.format("successfully read %d %s dependencies", dependencies.size(), fwdBwdText);
                log.info(msgSuccess);
                var statusResponse2 = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse2);

                var response = DependencyListResponse.fromDomain(dependencies);
                this.dependencyPresenter.present(response);
            } catch (RepoException exception) {
                var message = String.format("error reading %s dependencies: %s", fwdBwdText, exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing dependency list");

            this.mainState.getDependencyState().setDependencies(Collections.emptyList());

            var response = DependencyListResponse.fromDomain(Collections.emptyList());
            this.dependencyPresenter.present(response);
        }
    }
}
