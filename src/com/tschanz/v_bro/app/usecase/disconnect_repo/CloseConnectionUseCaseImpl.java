package com.tschanz.v_bro.app.usecase.disconnect_repo;

import com.tschanz.v_bro.app.usecase.disconnect_repo.requestmodel.CloseConnectionRequest;
import com.tschanz.v_bro.app.usecase.disconnect_repo.responsemodel.CloseConnectionResponse;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;

import java.util.logging.Logger;


public class CloseConnectionUseCaseImpl implements CloseConnectionUseCase {
    private final Logger logger = Logger.getLogger(CloseConnectionUseCaseImpl.class.getName());
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final CloseConnectionPresenter closeConnectionPresenter;


    public CloseConnectionUseCaseImpl(
        RepoServiceProvider<RepoService> repoServiceProvider,
        CloseConnectionPresenter closeConnectionPresenter
    ) {
        this.repoServiceProvider = repoServiceProvider;
        this.closeConnectionPresenter = closeConnectionPresenter;
    }


    @Override
    public void execute(CloseConnectionRequest request) {
        this.logger.info("UC: disconnecting from repo...");

        try {
            RepoService repoService = this.repoServiceProvider.getService(request.repoType);
            repoService.disconnect();

            String message = "disconnected successfully";
            this.logger.info(message);

            CloseConnectionResponse response = new CloseConnectionResponse(message, false);
            this.closeConnectionPresenter.present(response);
        } catch (RepoException exception) {
            String message = "error disconnectiong from repo: " + exception.getMessage();
            this.logger.severe(message);

            CloseConnectionResponse response = new CloseConnectionResponse(message, true);
            this.closeConnectionPresenter.present(response);
        }
    }
}
