package com.tschanz.v_bro.app.usecase.select_version_filter;

import com.tschanz.v_bro.app.usecase.common.responsemodel.VersionResponse;
import com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel.SelectVersionFilterRequest;
import com.tschanz.v_bro.app.usecase.select_version_filter.responsemodel.SelectVersionFilterResponse;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;
import com.tschanz.v_bro.versions.domain.service.VersionService;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class SelectVersionFilterUseCaseImpl implements SelectVersionFilterUseCase {
    private final Logger logger = Logger.getLogger(SelectVersionFilterUseCaseImpl.class.getName());
    private final RepoServiceProvider<VersionService> versionServiceProvider;
    private final SelectVersionFilterPresenter presenter;


    public SelectVersionFilterUseCaseImpl(
        RepoServiceProvider<VersionService> versionServiceProvider,
        SelectVersionFilterPresenter presenter
    ) {
        this.versionServiceProvider = versionServiceProvider;
        this.presenter = presenter;
    }


    @Override
    public void execute(SelectVersionFilterRequest request) {
        this.logger.info("UC: select version filters...");

        try {
            List<VersionInfo> versions;
            String message;
            if (request.elementClass != null && request.elementId != null) {
                VersionService versionService = this.versionServiceProvider.getService(request.repoType);
                versions = versionService.readVersionTimeline(request.elementClass, request.elementId); // TODO: version filter
                message = "successfully read " + versions.size() + " versions";
                this.logger.info(message);
            } else {
                versions = Collections.emptyList();
                message = "no class or element selected";
                this.logger.info(message);
            }

            SelectVersionFilterResponse response = new SelectVersionFilterResponse(
                this.getVersionResponse(versions),
                message,
                false
            );
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = "error reading versions: " + exception.getMessage();
            this.logger.severe(message);

            SelectVersionFilterResponse response = new SelectVersionFilterResponse(null, message, true);
            this.presenter.present(response);
        }
    }


    private List<VersionResponse> getVersionResponse(List<VersionInfo> versions) {
        return versions
            .stream()
            .map(version -> new VersionResponse(version.getId(), version.getGueltigVon(), version.getGueltigBis(), version.getPflegestatus()))
            .collect(Collectors.toList());
    }


    /*
    private VersionFilterItem getEffectiveVersionFilter(List<VersionItem> versions) {
        if (versions == null || versions.size() == 0) {
            return this.selectVersionFilterAction.getCurrentValue();
        }

        LocalDate filterVon = this.selectVersionFilterAction.getCurrentValue().getMinGueltigVon();
        LocalDate filterBis = this.selectVersionFilterAction.getCurrentValue().getMaxGueltigBis();
        VersionItem firstVersion = versions.get(0);
        VersionItem lastVersion = versions.get(versions.size() - 1);
        LocalDate effectiveVon;
        LocalDate effectiveBis;

        // von
        if (filterVon.isBefore(firstVersion.getGueltigVon().minusYears(1))) {
            effectiveVon = firstVersion.getGueltigVon().minusYears(1);
        } else {
            effectiveVon = firstVersion.getGueltigBis().isAfter(filterVon)
                ? filterVon
                : firstVersion.getGueltigBis().minusYears(1);
        }

        // bis
        if (filterBis.isAfter(lastVersion.getGueltigBis().plusYears(1))) {
            effectiveBis = lastVersion.getGueltigBis().plusYears(1);
        } else if (filterBis.equals(VersionInfo.HIGH_DATE)) {
            effectiveBis = lastVersion.getGueltigVon().isAfter(effectiveVon)
                ? lastVersion.getGueltigVon().plusYears(1)
                : effectiveVon.plusYears(1);
        } else {
            effectiveBis = lastVersion.getGueltigVon().isBefore(filterBis)
                ? filterBis
                : lastVersion.getGueltigVon().plusYears(1);
        }


        return new VersionFilterItem(
            effectiveVon,
            effectiveBis,
            this.selectVersionFilterAction.getCurrentValue().getMinPflegestatus()
        );
    }
     */
}
