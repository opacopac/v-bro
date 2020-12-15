package com.tschanz.v_bro.app.usecase.read_version_aggregate;

import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.data_structure.domain.model.VersionAggregate;
import com.tschanz.v_bro.data_structure.domain.service.VersionAggregateService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class ReadVersionAggregateUseCaseImpl implements ReadVersionAggregateUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<VersionAggregateService> versionAggregateServiceProvider;
    private final VersionAggregatePresenter presenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(ReadVersionAggregateRequest request) {
        var repoType = Objects.requireNonNull(mainState.getRepoState().getConnectionParameters().getRepoType());
        var elementClass = Objects.requireNonNull(mainState.getElementClassState().getSelectedName());
        var elementId = Objects.requireNonNull(mainState.getElementState().getCurrentElement().getId());
        var versionId = Objects.requireNonNull(request.getVersionId());

        try {
            log.info(String.format("UC: reading version aggregate of element class '%s' element id '%s' version id '%s'...", elementClass, elementId, versionId));

            VersionAggregateService versionAggregateService = this.versionAggregateServiceProvider.getService(repoType);
            VersionAggregate versionAggregate = versionAggregateService.readVersionAggregate(elementClass, elementId, versionId);
            this.mainState.getVersionAggregateState().setVersionAggregate(versionAggregate);

            String message = "successfully read version aggregate";
            log.info(message);
            var statusResponse = new StatusResponse(message, false);
            this.statusPresenter.present(statusResponse);

            var response = VersionAggregateResponse.fromDomain(versionAggregate);
            this.presenter.present(response);
        } catch (RepoException exception) {
            String message = String.format("error reading version aggregate: %s", exception.getMessage());
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }
    }
}
