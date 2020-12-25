package com.tschanz.v_bro.app.usecase.close_repo;

import com.tschanz.v_bro.app.presenter.repo.RepoPresenter;
import com.tschanz.v_bro.app.presenter.repo.RepoResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesRequest;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Objects;


@Log
@RequiredArgsConstructor
public class CloseRepoUseCaseImpl implements CloseRepoUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final RepoPresenter repoPresenter;
    private final StatusPresenter statusPresenter;
    private final ReadElementClassesUseCase readElementClassesUc;
    private final OpenElementClassUseCase openElementClassUc;


    @Override
    public void execute(CloseRepoRequest request) {
        var connectionParams = Objects.requireNonNull(this.mainState.getRepoState().getConnectionParameters());
        var repoType = connectionParams.getRepoType();

        try {
            var msgStart =  String.format("UC: disconnecting from repo %s...", repoType);
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false, true);
            this.statusPresenter.present(statusResponse1);

            var repoService = this.repoServiceProvider.getService(repoType);
            repoService.disconnect();

            this.mainState.getRepoState().setConnectionParameters(null);

            String msgSuccess = String.format("successfully disconnected from repo %s", repoType);
            log.info(msgSuccess);
            var statusResponse2 = new StatusResponse(msgSuccess, false, false);
            this.statusPresenter.present(statusResponse2);

            var repoResponse = RepoResponse.fromDomain(null);
            this.repoPresenter.present(repoResponse);
        } catch (RepoException exception) {
            var message = String.format("error disconnecting from repo: %s", exception.getMessage());
            log.severe(message);
            var statusResponse = new StatusResponse(message, true, false);
            this.statusPresenter.present(statusResponse);
        }

        var readElementClassesRequest = new ReadElementClassesRequest();
        this.readElementClassesUc.execute(readElementClassesRequest);

        var openElementClassRequest = new OpenElementClassRequest(null, true);
        this.openElementClassUc.execute(openElementClassRequest);
    }
}