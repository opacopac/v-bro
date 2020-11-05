package com.tschanz.v_bro.app.usecase.connect_repo;

import com.tschanz.v_bro.app.usecase.common.converter.ElementClassConverter;
import com.tschanz.v_bro.app.usecase.common.converter.RepoConnectionConverter;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.OpenConnectionRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.*;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;

import java.util.List;
import java.util.logging.Logger;


public class OpenConnectionUseCaseImpl implements OpenConnectionUseCase {
    private final Logger logger = Logger.getLogger(OpenConnectionUseCaseImpl.class.getName());
    private final RepoServiceProvider<RepoService> repoServiceProvider;
    private final RepoServiceProvider<ElementClassService> elementClassServiceProvider;
    private final OpenConnectionPresenter openConnectionPresenter;


    public OpenConnectionUseCaseImpl(
        RepoServiceProvider<RepoService> repoServiceProvider,
        RepoServiceProvider<ElementClassService> elementClassServiceProvider,
        OpenConnectionPresenter openConnectionPresenter
    ) {
        this.repoServiceProvider = repoServiceProvider;
        this.elementClassServiceProvider = elementClassServiceProvider;
        this.openConnectionPresenter = openConnectionPresenter;
    }


    @Override
    public void execute(OpenConnectionRequest request) {
        this.logger.info("UC: connecting to repo...");

        try {
            ConnectionParameters connectionParameters = RepoConnectionConverter.fromRequest(request.connectionParameters);
            RepoService repoService = this.repoServiceProvider.getService(connectionParameters.getType());
            repoService.connect(connectionParameters);

            ElementClassService elementClassService = this.elementClassServiceProvider.getService(connectionParameters.getType());
            List<ElementClass> elementClasses = elementClassService.readElementClasses();

            String message = "successfully connected to " + connectionParameters.getType() + " repo and read " + elementClasses.size()  + " element classes";
            this.logger.info(message);

            OpenConnectionResponse response = new OpenConnectionResponse(
                RepoConnectionConverter.toResponse(connectionParameters),
                ElementClassConverter.toResponse(elementClasses),
                message,
                false
            );
            this.openConnectionPresenter.present(response);
        } catch (RepoException exception) {
            String message = "error connecting to repo & reading element classes: " + exception.getMessage();
            this.logger.severe(message);

            OpenConnectionResponse response = new OpenConnectionResponse(null, null, message, true);
            this.openConnectionPresenter.present(response);
        }
    }
}
