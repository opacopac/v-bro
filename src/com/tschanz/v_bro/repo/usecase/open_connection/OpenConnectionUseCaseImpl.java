package com.tschanz.v_bro.repo.usecase.open_connection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;

import java.util.Map;
import java.util.logging.Logger;


public class OpenConnectionUseCaseImpl implements OpenConnectionUseCase {
    private final Logger logger = Logger.getLogger(OpenConnectionUseCaseImpl.class.getName());
    private final Map<RepoType, RepoService> repoMap;


    public OpenConnectionUseCaseImpl(Map<RepoType, RepoService> repoMap) {
        this.repoMap = repoMap;
    }


    @Override
    public OpenConnectionResponse connect(ConnectionParameters connectionParameters) throws VBroAppException {
        this.logger.info("UC: connecting to repo...");

        RepoService repoService = this.repoMap.get(connectionParameters.getType());
        try {
            repoService.connect(connectionParameters);

            this.logger.info("connected succesfully");
            return new OpenConnectionResponse(
                new OpenConnectionResponse.RepoConnection(connectionParameters.getType())
            );
        } catch (RepoException exception) {
            String message = "error connecting to repo: " + exception.getMessage();
            this.logger.severe(message);
            throw new VBroAppException(message, exception);
        }
    }
}
