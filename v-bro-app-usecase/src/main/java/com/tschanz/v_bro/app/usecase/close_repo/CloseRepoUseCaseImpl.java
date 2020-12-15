package com.tschanz.v_bro.app.usecase.close_repo;

import com.tschanz.v_bro.app.presenter.denominations.DenominationListResponse;
import com.tschanz.v_bro.app.presenter.denominations.DenominationsPresenter;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyListResponse;
import com.tschanz.v_bro.app.presenter.dependencies.DependencyPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementPresenter;
import com.tschanz.v_bro.app.presenter.element.ElementResponse;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListPresenter;
import com.tschanz.v_bro.app.presenter.element_class_list.ElementClassListResponse;
import com.tschanz.v_bro.app.presenter.repo.RepoPresenter;
import com.tschanz.v_bro.app.presenter.repo.RepoResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregatePresenter;
import com.tschanz.v_bro.app.presenter.version_aggregate.VersionAggregateResponse;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelinePresenter;
import com.tschanz.v_bro.app.presenter.version_timeline.VersionTimelineResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.common.selected_list.MultiSelectedList;
import com.tschanz.v_bro.common.selected_list.SelectedList;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Collections;
import java.util.Objects;


@Log
@RequiredArgsConstructor
public class CloseRepoUseCaseImpl implements CloseRepoUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final RepoPresenter repoPresenter;
    private final ElementClassListPresenter elementClassListPresenter;
    private final DenominationsPresenter denominationsPresenter;
    private final ElementPresenter elementPresenter;
    private final VersionTimelinePresenter versionTimelinePresenter;
    private final DependencyPresenter dependencyPresenter;
    private final VersionAggregatePresenter versionAggregatePresenter;
    private final StatusPresenter statusPresenter;


    @Override
    public void execute(CloseRepoRequest request) {
        var connectionParams = Objects.requireNonNull(this.mainState.getRepoState().getConnectionParameters());
        var repoType = connectionParams.getRepoType();

        try {
            log.info(String.format("UC: disconnecting from repo %s...", repoType));

            var repoService = this.repoServiceProvider.getService(repoType);
            repoService.disconnect();

            this.mainState.getRepoState().setConnectionParameters(null);

            String message = String.format("successfully disconnected from repo %s", repoType);
            log.info(message);
            var statusResponse = new StatusResponse(message, false);
            this.statusPresenter.present(statusResponse);

            var repoResponse = RepoResponse.fromDomain(null);
            this.repoPresenter.present(repoResponse);

            var elementClassListResponse = ElementClassListResponse.fromDomain(SelectedList.createEmpty());
            this.elementClassListPresenter.present(elementClassListResponse);

            var denominationListResponse = DenominationListResponse.fromDomain(MultiSelectedList.createEmpty());
            this.denominationsPresenter.present(denominationListResponse);

            var elementResponse = ElementResponse.fromDomain(null);
            this.elementPresenter.present(elementResponse);

            var versionTimelineResponse = VersionTimelineResponse.fromDomain(SelectedList.createEmpty());
            this.versionTimelinePresenter.present(versionTimelineResponse);

            var dependencyListResponse = DependencyListResponse.fromDomain(Collections.emptyList());
            this.dependencyPresenter.present(dependencyListResponse);

            var versionAggregateResponse = VersionAggregateResponse.fromDomain(null);
            this.versionAggregatePresenter.present(versionAggregateResponse);
        } catch (RepoException exception) {
            var message = String.format("error disconnecting from repo: %s", exception.getMessage());
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }
    }
}
