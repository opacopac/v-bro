package com.tschanz.v_bro.repo.usecase.close_connection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;

import java.util.Map;
import java.util.logging.Logger;


public class CloseConnectionUseCaseImpl implements CloseConnectionUseCase {
    private final Logger logger = Logger.getLogger(CloseConnectionUseCaseImpl.class.getName());
    private final Map<RepoType, RepoService> repoMap;


    public CloseConnectionUseCaseImpl(Map<RepoType, RepoService> repoMap) {
        this.repoMap = repoMap;
    }


    @Override
    public void disconnect() throws VBroAppException {
        this.logger.info("UC: disconnecting from repo...");

        try {
            for (RepoService repoService : this.repoMap.values()) {
                if (repoService.isConnected()) {
                    repoService.disconnect();
                }
            }
            this.logger.info("disconnected succesfully");
        } catch (RepoException exception) {
            String message = "error disconnectiong from repo: " + exception.getMessage();
            this.logger.severe(message);
            throw new VBroAppException(message, exception);
        }
    }
}
