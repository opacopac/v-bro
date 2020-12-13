package com.tschanz.v_bro.app.usecase.open_repo;

import com.tschanz.v_bro.app.presenter.repo.RepoPresenter;
import com.tschanz.v_bro.app.presenter.repo.RepoResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.MainState;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesRequest;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class OpenRepoUseCaseImpl implements OpenRepoUseCase {
    private final MainState mainState;
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final RepoPresenter repoPresenter;
    private final StatusPresenter statusPresenter;
    private final ReadElementClassesUseCase readElementClassesUc;


    @Override
    public void execute(OpenRepoRequest request) {
        var connectionParameters = ConnectionParametersRequest.fromRequest(request.getConnectionParameters());

        try {
            log.info("UC: connecting to repo...");

            var repoService = this.repoServiceProvider.getService(connectionParameters.getRepoType());
            repoService.connect(connectionParameters);

            this.mainState.getRepoState().setConnectionParameters(connectionParameters);

            String message = String.format("successfully connected to %s repo", connectionParameters.getRepoType());
            log.info(message);
            var statusResponse = new StatusResponse(message, false);
            this.statusPresenter.present(statusResponse);

            var response = RepoResponse.fromDomain(connectionParameters);
            this.repoPresenter.present(response);

            var readElementClassesRequest = new ReadElementClassesRequest(true);
            this.readElementClassesUc.execute(readElementClassesRequest);
        } catch (RepoException exception) {
            var message = "error connecting to repo: " + exception.getMessage();
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }
    }
}
