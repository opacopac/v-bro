package com.tschanz.v_bro.app.usecase.select_dependency_filter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.requestmodel.SelectDependencyFilterRequest;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.SelectDependencyFilterResponse;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class SelectDependencyFilterUseCaseImpl implements SelectDependencyFilterUseCase {
    private final Logger logger = Logger.getLogger(SelectDependencyFilterUseCaseImpl.class.getName());
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final SelectDependencyFilterPresenter presenter;


    public SelectDependencyFilterUseCaseImpl(
        RepoServiceProvider<DependencyService> dependencyServiceProvider,
        SelectDependencyFilterPresenter presenter
    ) {
        this.dependencyServiceProvider = dependencyServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectDependencyFilterRequest request) {
        this.logger.info("UC: select dependency filter...");

        try {
            DependencyService dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(request.elementClass, request.elementId, request.versionId);

            String message = "successfully read " + fwdDependencies.size() + " FWD dependencies";
            this.logger.info(message);

            SelectDependencyFilterResponse response = new SelectDependencyFilterResponse(
                this.getFwdDependencyResponse(fwdDependencies),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading dependencies and version aggregate: " + exception.getMessage();
            this.logger.severe(message);

            SelectDependencyFilterResponse response = new SelectDependencyFilterResponse(null, message, true);
            this.presenter.present(response);
        }
    }


    private List<FwdDependencyResponse> getFwdDependencyResponse(List<FwdDependency> dependencies) {
        return dependencies
            .stream()
            .map(fwdDep -> new FwdDependencyResponse(
                fwdDep.elementName(),
                fwdDep.elementId(),
                fwdDep.getVersions()
                    .stream()
                    .map(version -> new VersionResponse(
                        version.getId(),
                        version.getGueltigVon(),
                        version.getGueltigBis(),
                        version.getPflegestatus()
                    ))
                    .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }
}