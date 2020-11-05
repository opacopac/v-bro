package com.tschanz.v_bro.app.usecase.select_element;

import com.tschanz.v_bro.app.usecase.common.requestmodel.VersionFilterRequest;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionFilterResponse;
import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.app.usecase.select_element.responsemodel.SelectElementResponse;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.versions.domain.model.VersionData;
import com.tschanz.v_bro.versions.domain.model.VersionFilter;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class SelectElementUseCaseImpl implements SelectElementUseCase {
    private final Logger logger = Logger.getLogger(SelectElementUseCaseImpl.class.getName());
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final SelectElementPresenter presenter;


    public SelectElementUseCaseImpl(
        RepoServiceProvider<VersionService> versionServiceProvider,
        SelectElementPresenter presenter
    ) {
        this.versionServiceProvider = versionServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectElementRequest request) {
        this.logger.info("UC: select element '" + request.elementId + "'...");

        try {
            VersionFilter effectiveVersionFilter;
            List<VersionData> versions;
            String message;
            if (request.elementClass != null && request.elementId != null) {
                VersionService versionService = this.versionServiceProvider.getService(request.repoType);
                versions = versionService.readVersionTimeline(request.elementClass, request.elementId);

                effectiveVersionFilter = this.getEffectiveVersionFilter(request.versionFilter, versions);

                message = "successfully read " + versions.size() + " versions, ";
                message += " displaying timeline between " + effectiveVersionFilter.getMinGueltigVon() + " till " + effectiveVersionFilter.getMaxGueltigBis();
                this.logger.info(message);
            } else {
                effectiveVersionFilter = null;
                versions = Collections.emptyList();
                message = "no class or element selected";
                this.logger.info(message);
            }

            SelectElementResponse response = new SelectElementResponse(
                this.getEffectiveVersionFilterResponse(effectiveVersionFilter),
                this.getVersionResponse(versions),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading versions: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementResponse response = new SelectElementResponse(null, null, message, true);
            this.presenter.present(response);
        }
    }


    private VersionFilter getEffectiveVersionFilter(VersionFilterRequest selectedVersionFilterRequest, List<VersionData> versions) {
        VersionFilter selectedVersionFilter = new VersionFilter(
            selectedVersionFilterRequest.minGueltigVon,
            selectedVersionFilterRequest.maxGueltigBis,
            selectedVersionFilterRequest.minPflegestatus
        );

        return selectedVersionFilter.cropToVersions(versions);
    }


    private VersionFilterResponse getEffectiveVersionFilterResponse(VersionFilter effectiveVersionFilter) {
        if (effectiveVersionFilter != null) {
            return new VersionFilterResponse(
                effectiveVersionFilter.getMinGueltigVon(),
                effectiveVersionFilter.getMaxGueltigBis(),
                effectiveVersionFilter.getMinPflegestatus()
            );
        } else {
            return null;
        }
    }


    private List<VersionResponse> getVersionResponse(List<VersionData> versions) {
        return versions
            .stream()
            .map(version -> new VersionResponse(version.getId(), version.getGueltigVon(), version.getGueltigBis(), version.getPflegestatus()))
            .collect(Collectors.toList());
    }
}

