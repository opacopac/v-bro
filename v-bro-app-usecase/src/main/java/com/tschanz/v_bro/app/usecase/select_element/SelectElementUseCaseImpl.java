package com.tschanz.v_bro.app.usecase.select_element;

import com.tschanz.v_bro.app.usecase.common.converter.FwdDependencyConverter;
import com.tschanz.v_bro.app.usecase.common.converter.VersionAggregateConverter;
import com.tschanz.v_bro.app.usecase.common.converter.VersionConverter;
import com.tschanz.v_bro.app.usecase.common.converter.VersionFilterConverter;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.app.usecase.select_element.responsemodel.SelectElementResponse;
import com.tschanz.v_bro.structure.domain.model.FwdDependency;
import com.tschanz.v_bro.structure.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.structure.domain.model.VersionData;
import com.tschanz.v_bro.structure.domain.model.VersionFilter;
import com.tschanz.v_bro.structure.domain.service.VersionService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class SelectElementUseCaseImpl implements SelectElementUseCase {
    private final Logger logger = Logger.getLogger(SelectElementUseCaseImpl.class.getName());
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final SelectElementPresenter presenter;


    public SelectElementUseCaseImpl(
        RepoServiceProvider<VersionService> versionServiceProvider,
        RepoServiceProvider<DependencyService> dependencyServiceProvider,
        RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider,
        SelectElementPresenter presenter
    ) {
        this.versionServiceProvider = versionServiceProvider;
        this.dependencyServiceProvider = dependencyServiceProvider;
        this.versionAggregateServiceProvider = versionAggregateServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectElementRequest request) {
        this.logger.info("UC: select element '" + request.elementId + "'...");

        try {
            // versions
            VersionService versionService = this.versionServiceProvider.getService(request.repoType);
            List<VersionData> versions = versionService.readVersionTimeline(request.elementClass, request.elementId);
            String selectVersionId = versions.size() > 0 ? versions.get(versions.size() - 1).getId() : null;

            // version filter
            VersionFilter selectedVersionFilter = VersionFilterConverter.fromRequest(request.versionFilter);
            VersionFilter effectiveVersionFilter = selectedVersionFilter.cropToVersions(versions);

            // dependencies
            DependencyService dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = selectVersionId != null ? dependencyService.readFwdDependencies(request.elementClass, request.elementId, selectVersionId) : Collections.emptyList();

            // version aggregate
            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(request.repoType);
            VersionAggregate versionAggregate = selectVersionId != null ? versionAggregateService.readVersionAggregate(request.elementClass, request.elementId, selectVersionId) : null;

            String message = "successfully read " + versions.size() + " versions, ";
            message += " displaying timeline between " + effectiveVersionFilter.getMinGueltigVon() + " till " + effectiveVersionFilter.getMaxGueltigBis();
            this.logger.info(message);

            SelectElementResponse response = new SelectElementResponse(
                VersionConverter.toResponse(versions),
                VersionFilterConverter.toResponse(effectiveVersionFilter),
                FwdDependencyConverter.toResponse(fwdDependencies),
                VersionAggregateConverter.toResponse(versionAggregate),
                request.elementId,
                selectVersionId,
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading versions: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementResponse response = new SelectElementResponse(null, null, null, null, null, null, message, true);
            this.presenter.present(response);
        }
    }
}
