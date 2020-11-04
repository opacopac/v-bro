package com.tschanz.v_bro.app.usecase.select_version;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_dependency_filter.responsemodel.FwdDependencyResponse;
import com.tschanz.v_bro.app.usecase.select_version.requestmodel.SelectVersionRequest;
import com.tschanz.v_bro.app.usecase.select_version.responsemodel.SelectVersionResponse;
import com.tschanz.v_bro.app.usecase.select_version.responsemodel.VersionAggregateNodeResponse;
import com.tschanz.v_bro.app.usecase.select_version.responsemodel.VersionAggregateResponse;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.version_aggregates.domain.model.AggregateNode;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class SelectVersionUseCaseImpl implements SelectVersionUseCase {
    private final Logger logger = Logger.getLogger(SelectVersionUseCaseImpl.class.getName());
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final SelectVersionPresenter presenter;


    public SelectVersionUseCaseImpl(
        RepoServiceProvider<DependencyService> dependencyServiceProvider,
        RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider,
        SelectVersionPresenter presenter
    ) {
        this.dependencyServiceProvider = dependencyServiceProvider;
        this.versionAggregateServiceProvider = versionAggregateServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectVersionRequest request) {
        this.logger.info("UC: select version '" + request.versionId + "'...");

        try {
            DependencyService dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(request.elementClass, request.elementId, request.versionId);

            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(request.repoType);
            VersionAggregate versionAggregate = versionAggregateService.readVersionAggregate(request.elementClass, request.elementId, request.versionId);

            String message = "successfully read " + fwdDependencies.size() + " FWD dependencies and version aggregate";
            this.logger.info(message);

            SelectVersionResponse response = new SelectVersionResponse(
                this.getFwdDependencyResponse(fwdDependencies),
                this.getVersionAggregateResponse(versionAggregate),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading dependencies and version aggregate: " + exception.getMessage();
            this.logger.severe(message);

            SelectVersionResponse response = new SelectVersionResponse(null, null, message, true);
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


    private VersionAggregateResponse getVersionAggregateResponse(VersionAggregate versionAggregate) {
        return new VersionAggregateResponse(
            this.getAggregateNodeResponse(versionAggregate.getRootNode())
        );
    }


    private VersionAggregateNodeResponse getAggregateNodeResponse(AggregateNode node) {
        return new VersionAggregateNodeResponse(
            node.getNodeName(),
            node.getFieldValues(),
            node.getChildNodes()
                .stream()
                .map(this::getAggregateNodeResponse)
                .collect(Collectors.toList())
        );
    }
}
