package com.tschanz.v_bro.app.usecase.select_element;

import com.tschanz.v_bro.app.usecase.common.converter.VersionConverter;
import com.tschanz.v_bro.app.usecase.common.converter.VersionFilterConverter;
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

                VersionFilter selectedVersionFilter = VersionFilterConverter.fromRequest(request.versionFilter);
                effectiveVersionFilter = selectedVersionFilter.cropToVersions(versions);

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
                VersionFilterConverter.toResponse(effectiveVersionFilter),
                VersionConverter.toResponse(versions),
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
}
