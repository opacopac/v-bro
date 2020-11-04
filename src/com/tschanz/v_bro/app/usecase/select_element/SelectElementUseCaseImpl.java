package com.tschanz.v_bro.app.usecase.select_element;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_element.requestmodel.SelectElementRequest;
import com.tschanz.v_bro.app.usecase.select_element.responsemodel.SelectElementResponse;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.domain.service.VersionService;

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
            VersionService versionService = this.versionServiceProvider.getService(request.repoType);
            List<VersionInfo> versions = versionService.readVersionTimeline(request.elementClass, request.elementId);

            String message = "successfully read " + versions.size() + " versions";
            this.logger.info(message);

            SelectElementResponse response = new SelectElementResponse(
                this.getVersionResponse(versions),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading versions: " + exception.getMessage();
            this.logger.severe(message);

            SelectElementResponse response = new SelectElementResponse(null, message, true);
            this.presenter.present(response);
        }
    }


    private List<VersionResponse> getVersionResponse(List<VersionInfo> versions) {
        return versions
            .stream()
            .map(version -> new VersionResponse(version.getId(), version.getGueltigVon(), version.getGueltigBis(), version.getPflegestatus()))
            .collect(Collectors.toList());
    }
}

