package com.tschanz.v_bro.app.usecase.read_version_aggregate;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class ReadVersionAggregateUseCaseImpl implements ReadVersionAggregateUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final VersionAggregatePresenter presenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadVersionAggregateRequest request) {
        var repoType = mainState.getRepoState().getCurrentRepoType();
        var version = mainState.getVersionState().getCurrentVersion();

        if (repoType != null && version != null) {
            try {
                var msgStart = String.format("UC: reading version aggregate of version id '%s'...", version.getId());
                log.info(msgStart);
                var statusResponse1 = new StatusResponse(msgStart, false, true);
                this.statusPresenter.present(statusResponse1);

                VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(repoType);
                VersionAggregate versionAggregate = versionAggregateService.readVersionAggregate(version);
                this.mainState.getVersionAggregateState().setVersionAggregate(versionAggregate);

                String msgSuccess = "successfully read version aggregate";
                log.info(msgSuccess);
                var statusResponse2 = new StatusResponse(msgSuccess, false, false);
                this.statusPresenter.present(statusResponse2);

                var response = VersionAggregateResponse.fromDomain(versionAggregate);
                this.presenter.present(response);
            } catch (RepoException exception) {
                String message = String.format("error reading version aggregate: %s", exception.getMessage());
                log.severe(message);
                var statusResponse = new StatusResponse(message, true, false);
                this.statusPresenter.present(statusResponse);
            }
        } else {
            log.info("UC: clearing version aggregate");

            this.mainState.getVersionAggregateState().setVersionAggregate(null);

            var response = VersionAggregateResponse.fromDomain(null);
            this.presenter.present(response);
        }
    }
}
