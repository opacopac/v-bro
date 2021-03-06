package com.tschanz.v_bro.app.usecase.open_repo;

import com.tschanz.v_bro.app.presenter.repo_connection.RepoConnectionPresenter;
import com.tschanz.v_bro.app.presenter.repo_connection.RepoResponse;
import com.tschanz.v_bro.app.presenter.status.StatusPresenter;
import com.tschanz.v_bro.app.presenter.status.StatusResponse;
import com.tschanz.v_bro.app.state.AppState;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassRequest;
import com.tschanz.v_bro.app.usecase.open_element_class.OpenElementClassUseCase;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesRequest;
import com.tschanz.v_bro.app.usecase.read_element_classes.ReadElementClassesUseCase;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoConnectionService;
import com.tschanz.v_bro.app.service.RepoServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@RequiredArgsConstructor
public class OpenRepoUseCaseImpl implements OpenRepoUseCase {
    private final AppState appState;
    private final RepoServiceProvider<RepoConnectionService> repoServiceProvider;
    private final RepoConnectionPresenter repoConnectionPresenter;
    private final StatusPresenter statusPresenter;
    private final ReadElementClassesUseCase readElementClassesUc;
    private final OpenElementClassUseCase openElementClassUc;


    @Override
    public void execute(OpenRepoRequest request) {
        var connectionParameters = ConnectionParametersRequest.fromRequest(request.getConnectionParameters());

        try {
            var msgStart = "UC: connecting to repo & indexing structure...";
            log.info(msgStart);
            var statusResponse1 = new StatusResponse(msgStart, false);
            this.statusPresenter.present(statusResponse1);

            var repoService = this.repoServiceProvider.getService(connectionParameters.getRepoType());
            repoService.connect(connectionParameters);

            this.appState.setConnectionParameters(connectionParameters);

            var msgSuccess = String.format("successfully connected to %s repo", connectionParameters.getRepoType());
            log.info(msgSuccess);
            var statusResponse2 = new StatusResponse(msgSuccess, false);
            this.statusPresenter.present(statusResponse2);

            var response = RepoResponse.fromDomain(connectionParameters);
            this.repoConnectionPresenter.present(response);
        } catch (RepoException exception) {
            var message = "error connecting to repo: " + exception.getMessage();
            log.severe(message);
            var statusResponse = new StatusResponse(message, true);
            this.statusPresenter.present(statusResponse);
        }

        var readElementClassesRequest = new ReadElementClassesRequest();
        this.readElementClassesUc.execute(readElementClassesRequest);

        var classes = this.appState.getElementClasses().getItems();
        var selectClass = classes.size() > 0
            ? classes.get(0).getName()
            : null;
        var openElementClassRequest = new OpenElementClassRequest(selectClass, true);
        this.openElementClassUc.execute(openElementClassRequest);
    }
}
