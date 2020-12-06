package com.tschanz.v_bro.app.usecase.connect_repo;

import com.tschanz.v_bro.app.usecase.common.converter.*;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.OpenConnectionRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.OpenConnectionResponse;
import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.*;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.List;


@Log
@RequiredArgsConstructor
public class OpenConnectionUseCaseImpl implements OpenConnectionUseCase {
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final OpenConnectionPresenter openConnectionPresenter;


    @Override
    public void execute(OpenConnectionRequest request) {
        log.info("UC: connecting to repo...");

        try {
            // repo connection
            var connectionParameters = RepoConnectionConverter.fromRequest(request.connectionParameters);
            var repoService = this.repoServiceProvider.getService(connectionParameters.getRepoType());
            repoService.connect(connectionParameters);

            // element classes
            var elementClassService = this.elementClassServiceProvider.getService(connectionParameters.getRepoType());
            var elementClasses = elementClassService.readElementClasses();
            var selectElementClass = elementClasses.size() > 0 ? elementClasses.get(0).getName() : null;

            // element denominations
            List<Denomination> denominations = selectElementClass != null ? elementClassService.readDenominations(selectElementClass) : Collections.emptyList();
            List<String> selectDenominations = denominations.size() > 0 ? List.of(denominations.get(0).getName()) : Collections.emptyList();

            // elements
            var elementService = this.elementServiceProvider.getService(connectionParameters.getRepoType());
            List<ElementData> elements = selectElementClass != null ? elementService.readElements(selectElementClass, selectDenominations, "", 100) : Collections.emptyList(); // TODO
            var selectElementId = elements.size() > 0 ? elements.get(0).getId() : null;

            // versions
            var versionService = this.versionServiceProvider.getService(connectionParameters.getRepoType());
            List<VersionData> versions = selectElementId != null ? versionService.readVersionTimeline(selectElementClass, selectElementId) : Collections.emptyList();
            var selectVersionId = versions.size() > 0 ? versions.get(versions.size() - 1).getId() : null;

            // version filter
            var versionFilter = VersionFilter.DEFAULT_VERSION_FILTER;
            var effectiveVersionFilter = versionFilter.cropToVersions(versions);

            // dependencies
            var dependencyService = this.dependencyServiceProvider.getService(connectionParameters.getRepoType());
            List<FwdDependency> fwdDependencies = selectVersionId != null ? dependencyService.readFwdDependencies(selectElementClass, selectElementId, selectVersionId) : Collections.emptyList();

            // version aggregate
            var versionAggregateService = this.versionAggregateServiceProvider.getService(connectionParameters.getRepoType());
            var versionAggregate = selectVersionId != null ? versionAggregateService.readVersionAggregate(selectElementClass, selectElementId, selectVersionId) : null;

            String message = "successfully connected to " + connectionParameters.getRepoType() + " repo and read "
                + elementClasses.size()  + " element classes, " + elements.size()  + " elements, " + versions.size()  + " versions, "
                + fwdDependencies.size() + " dependencies and the version aggregate";
            log.info(message);

            var response = new OpenConnectionResponse(
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
            var message = "error connecting to repo & reading classes/elements/versions: " + exception.getMessage();
            log.severe(message);

            var response = new OpenConnectionResponse(message);
            this.openConnectionPresenter.present(response);
        }
    }
}
