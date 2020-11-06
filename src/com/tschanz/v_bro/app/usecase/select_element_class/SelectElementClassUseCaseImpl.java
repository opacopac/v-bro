package com.tschanz.v_bro.app.usecase.select_element_class;

import com.tschanz.v_bro.app.usecase.common.converter.*;
import com.tschanz.v_bro.app.usecase.select_element_class.requestmodel.SelectElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_element_class.responsemodel.SelectElementClassResponse;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.domain.model.VersionFilter;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class SelectElementClassUseCaseImpl implements SelectElementClassUseCase {
    private final Logger logger = Logger.getLogger(SelectElementClassUseCaseImpl.class.getName());
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final SelectElementClassPresenter presenter;


    public SelectElementClassUseCaseImpl(
        RepoServiceProvider<ElementClassService> elementClassServiceProvider,
        RepoServiceProvider<ElementService> elementServiceProvider,
        RepoServiceProvider<VersionService> versionServiceProvider,
        RepoServiceProvider<DependencyService> dependencyServiceProvider,
        RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider,
        SelectElementClassPresenter presenter
    ) {
        this.elementClassServiceProvider = elementClassServiceProvider;
        this.elementServiceProvider = elementServiceProvider;
        this.versionServiceProvider = versionServiceProvider;
        this.dependencyServiceProvider = dependencyServiceProvider;
        this.versionAggregateServiceProvider = versionAggregateServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectElementClassRequest request) {
        this.logger.info("UC: select element class '" + request.elementClass + "'...");

        try {
            // element denominations
            ElementClassService elementClassService = this.elementClassServiceProvider.getService(request.repoType);
            List<Denomination> denominations = elementClassService.readDenominations(request.elementClass);
            List<String> selectDenominations = denominations.size() > 0 ? List.of(denominations.get(0).getName()) : Collections.emptyList();

            // elements
            ElementService elementService = this.elementServiceProvider.getService(request.repoType);
            List<ElementData> elements = elementService.readElements(request.elementClass, selectDenominations);
            String selectElementId = elements.size() > 0 ? elements.get(0).getId() : null;

            // versions
            VersionService versionService = this.versionServiceProvider.getService(request.repoType);
            List<VersionData> versions = selectElementId != null ? versionService.readVersionTimeline(request.elementClass, selectElementId) : Collections.emptyList();
            String selectVersionId = versions.size() > 0 ? versions.get(0).getId() : null;

            // version filter
            VersionFilter selectedVersionFilter = VersionFilterConverter.fromRequest(request.versionFilter);
            VersionFilter effectiveVersionFilter = selectedVersionFilter.cropToVersions(versions);

            // dependencies
            DependencyService dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = selectVersionId != null ? dependencyService.readFwdDependencies(request.elementClass, selectElementId, selectVersionId) : Collections.emptyList();

            // version aggregate
            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(request.repoType);
            VersionAggregate versionAggregate = selectVersionId != null ? versionAggregateService.readVersionAggregate(request.elementClass, selectElementId, selectVersionId) : null;

            String message = "successfully read " + denominations.size() + " denominations,  " + elements.size() + " elements"
                + versions.size()  + " versions, " + fwdDependencies.size() + " dependencies and the version aggregate";
            this.logger.info(message);

            SelectElementClassResponse response = new SelectElementClassResponse(
                DenominationConverter.toResponse(denominations),
                ElementConverter.toResponse(elements),
                VersionConverter.toResponse(versions),
                VersionFilterConverter.toResponse(effectiveVersionFilter),
                FwdDependencyConverter.toResponse(fwdDependencies),
                VersionAggregateConverter.toResponse(versionAggregate),
                selectDenominations,
                request.elementClass,
                selectElementId,
                selectVersionId,
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading element denominations and elements: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementClassResponse response = new SelectElementClassResponse(null, null, null, null, null,
                null, null, null, null, null, message, true);
            this.presenter.present(response);
        }
    }
}
