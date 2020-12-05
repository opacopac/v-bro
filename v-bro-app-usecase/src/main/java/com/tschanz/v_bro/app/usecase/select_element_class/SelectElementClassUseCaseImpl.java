package com.tschanz.v_bro.app.usecase.select_element_class;

import com.tschanz.v_bro.app.usecase.common.converter.*;
import com.tschanz.v_bro.app.usecase.select_element_class.requestmodel.SelectElementClassRequest;
import com.tschanz.v_bro.app.usecase.select_element_class.responsemodel.SelectElementClassResponse;
import com.tschanz.v_bro.data_structure.domain.model.*;
import com.tschanz.v_bro.data_structure.domain.service.*;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


@RequiredArgsConstructor
public class SelectElementClassUseCaseImpl implements SelectElementClassUseCase {
    private final Logger logger = Logger.getLogger(SelectElementClassUseCaseImpl.class.getName());
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final RepoServiceProvider<ElementService> elementServiceProvider;
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final RepoServiceProvider<DependencyService> dependencyServiceProvider;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final SelectElementClassPresenter presenter;


    @Override
    public void execute(SelectElementClassRequest request) {
        this.logger.info("UC: select element class '" + request.elementClass + "'...");

        try {
            // element denominations
            var elementClassService = this.elementClassServiceProvider.getService(request.repoType);
            var denominations = elementClassService.readDenominations(request.elementClass);
            List<String> selectDenominations = denominations.size() > 0 ? List.of(denominations.get(0).getName()) : Collections.emptyList();

            // elements
            var elementService = this.elementServiceProvider.getService(request.repoType);
            var elements = elementService.readElements(request.elementClass, selectDenominations);
            var selectElementId = elements.size() > 0 ? elements.get(0).getId() : null;

            // versions
            var versionService = this.versionServiceProvider.getService(request.repoType);
            List<VersionData> versions = selectElementId != null ? versionService.readVersionTimeline(request.elementClass, selectElementId) : Collections.emptyList();
            var selectVersionId = versions.size() > 0 ? versions.get(versions.size() - 1).getId() : null;

            // version filter
            var selectedVersionFilter = VersionFilterConverter.fromRequest(request.versionFilter);
            var effectiveVersionFilter = selectedVersionFilter.cropToVersions(versions);

            // dependencies
            var dependencyService = this.dependencyServiceProvider.getService(request.repoType);
            List<FwdDependency> fwdDependencies = selectVersionId != null ? dependencyService.readFwdDependencies(request.elementClass, selectElementId, selectVersionId) : Collections.emptyList();

            // version aggregate
            var versionAggregateService = this.versionAggregateServiceProvider.getService(request.repoType);
            var versionAggregate = selectVersionId != null ? versionAggregateService.readVersionAggregate(request.elementClass, selectElementId, selectVersionId) : null;

            var message = "successfully read " + denominations.size() + " denominations,  " + elements.size() + " elements"
                + versions.size()  + " versions, " + fwdDependencies.size() + " dependencies and the version aggregate";
            this.logger.info(message);

            var response = new SelectElementClassResponse(
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
            var message = "error reading element denominations and elements: " + exception.getMessage();
            this.logger.severe(message);

            var response = new SelectElementClassResponse(null, null, null, null, null,
                null, null, null, null, null, message, true);
            this.presenter.present(response);
        }
    }
}
