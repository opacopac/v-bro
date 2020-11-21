package com.tschanz.v_bro.app.usecase.connect_repo;

import com.tschanz.v_bro.app.usecase.common.converter.*;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.OpenConnectionRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.*;
import com.tschanz.v_bro.data_structure.domain.model.FwdDependency;
import com.tschanz.v_bro.data_structure.domain.service.DependencyService;
import com.tschanz.v_bro.data_structure.domain.model.Denomination;
import com.tschanz.v_bro.data_structure.domain.model.ElementClass;
import com.tschanz.v_bro.data_structure.domain.service.ElementClassService;
import com.tschanz.v_bro.data_structure.domain.model.ElementData;
import com.tschanz.v_bro.data_structure.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.data_structure.domain.model.VersionData;
import com.tschanz.v_bro.data_structure.domain.model.VersionFilter;
import com.tschanz.v_bro.data_structure.domain.service.VersionService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class OpenConnectionUseCaseImpl implements OpenConnectionUseCase {
    private final Logger logger = Logger.getLogger(OpenConnectionUseCaseImpl.class.getName());
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final OpenConnectionPresenter openConnectionPresenter;


    public OpenConnectionUseCaseImpl(
        RepoServiceProvider<RepoService> repoServiceProvider,
        RepoServiceProvider<ElementClassService> elementClassServiceProvider,
        RepoServiceProvider<ElementService> elementServiceProvider,
        RepoServiceProvider<VersionService> versionServiceProvider,
        RepoServiceProvider<DependencyService> dependencyServiceProvider,
        RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider,
        OpenConnectionPresenter openConnectionPresenter
    ) {
        this.repoServiceProvider = repoServiceProvider;
        this.elementClassServiceProvider = elementClassServiceProvider;
        this.elementServiceProvider = elementServiceProvider;
        this.versionServiceProvider = versionServiceProvider;
        this.dependencyServiceProvider = dependencyServiceProvider;
        this.versionAggregateServiceProvider = versionAggregateServiceProvider;
        this.openConnectionPresenter = openConnectionPresenter;
    }


    @Override
    public void execute(OpenConnectionRequest request) {
        this.logger.info("UC: connecting to repo...");

        try {
            // repo connection
            ConnectionParameters connectionParameters = RepoConnectionConverter.fromRequest(request.connectionParameters);
            RepoService repoService = this.repoServiceProvider.getService(connectionParameters.getRepoType());
            repoService.connect(connectionParameters);

            // element classes
            ElementClassService elementClassService = this.elementClassServiceProvider.getService(connectionParameters.getRepoType());
            List<ElementClass> elementClasses = elementClassService.readElementClasses();
            String selectElementClass = elementClasses.size() > 0 ? elementClasses.get(0).getName() : null;

            // element denominations
            List<Denomination> denominations = selectElementClass != null ? elementClassService.readDenominations(selectElementClass) : Collections.emptyList();
            List<String> selectDenominations = denominations.size() > 0 ? List.of(denominations.get(0).getName()) : Collections.emptyList();

            // elements
            ElementService elementService = this.elementServiceProvider.getService(connectionParameters.getRepoType());
            List<ElementData> elements = selectElementClass != null ? elementService.readElements(selectElementClass, selectDenominations) : Collections.emptyList();
            String selectElementId = elements.size() > 0 ? elements.get(0).getId() : null;

            // versions
            VersionService versionService = this.versionServiceProvider.getService(connectionParameters.getRepoType());
            List<VersionData> versions = selectElementId != null ? versionService.readVersionTimeline(selectElementClass, selectElementId) : Collections.emptyList();
            String selectVersionId = versions.size() > 0 ? versions.get(versions.size() - 1).getId() : null;

            // version filter
            VersionFilter versionFilter = VersionFilter.DEFAULT_VERSION_FILTER;
            VersionFilter effectiveVersionFilter = versionFilter.cropToVersions(versions);

            // dependencies
            DependencyService dependencyService = this.dependencyServiceProvider.getService(connectionParameters.getRepoType());
            List<FwdDependency> fwdDependencies = selectVersionId != null ? dependencyService.readFwdDependencies(selectElementClass, selectElementId, selectVersionId) : Collections.emptyList();

            // version aggregate
            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(connectionParameters.getRepoType());
            VersionAggregate versionAggregate = selectVersionId != null ? versionAggregateService.readVersionAggregate(selectElementClass, selectElementId, selectVersionId) : null;

            String message = "successfully connected to " + connectionParameters.getRepoType() + " repo and read "
                + elementClasses.size()  + " element classes, " + elements.size()  + " elements, " + versions.size()  + " versions, "
                + fwdDependencies.size() + " dependencies and the version aggregate";
            this.logger.info(message);

            OpenConnectionResponse response = new OpenConnectionResponse(
                RepoConnectionConverter.toResponse(connectionParameters),
                ElementClassConverter.toResponse(elementClasses),
                DenominationConverter.toResponse(denominations),
                ElementConverter.toResponse(elements),
                VersionConverter.toResponse(versions),
                VersionFilterConverter.toResponse(versionFilter),
                VersionFilterConverter.toResponse(effectiveVersionFilter),
                FwdDependencyConverter.toResponse(fwdDependencies),
                VersionAggregateConverter.toResponse(versionAggregate),
                selectElementClass,
                selectDenominations,
                selectElementId,
                selectVersionId,
                message,
                false
            );
            this.openConnectionPresenter.present(response);
        } catch (RepoException exception) {
            String message = "error connecting to repo & reading classes/elements/versions: " + exception.getMessage();
            this.logger.severe(message);

            OpenConnectionResponse response = new OpenConnectionResponse(null, null, null, null, null, null,
                null, null, null, null, null, null, null, message, true);
            this.openConnectionPresenter.present(response);
        }
    }
}
