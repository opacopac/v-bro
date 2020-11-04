package com.tschanz.v_bro.app.usecase.connect_repo;

import com.tschanz.v_bro.app.usecase.common.responsemodel.ElementClassResponse;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.ConnectionParametersRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.JdbcConnectionParametersRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.OpenConnectionRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.XMlConnectionParametersRequest;
import com.tschanz.v_bro.app.usecase.connect_repo.responsemodel.*;
import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;
import com.tschanz.v_bro.repo.domain.service.RepoService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.domain.service.RepoServiceProvider;
import com.tschanz.v_bro.repo.persistence.jdbc.model.JdbcConnectionParameters;
import com.tschanz.v_bro.repo.persistence.mock.model.MockConnectionParameters;
import com.tschanz.v_bro.repo.persistence.xml.model.XmlConnectionParameters;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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
            ConnectionParameters connectionParameters = this.openConnection(request.connectionParameters);
            List<ElementClass> elementClasses = this.readElementClasses(connectionParameters.getType());
            String message = "successfully connected to " + connectionParameters.getType() + " repo and read " + elementClasses.size()  + " element classes";
            this.logger.info(message);

            OpenConnectionResponse response = new OpenConnectionResponse(
                this.getResponseRepoConnection(connectionParameters),
                this.getResponseElementClasses(elementClasses),
                message,
                false
            );
            this.openConnectionPresenter.present(response);
        } catch (RepoException | VBroAppException exception) {
            String message = "error connecting to repo & reading element classes: " + exception.getMessage();
            this.logger.severe(message);

            OpenConnectionResponse response = new OpenConnectionResponse(null, null, message, true);
            this.openConnectionPresenter.present(response);
        }
    }


    private ConnectionParameters openConnection(ConnectionParametersRequest requestConnectionParameters) throws RepoException {
        ConnectionParameters connectionParameters;
        switch (requestConnectionParameters.repoType) {
            case JDBC:
                JdbcConnectionParametersRequest jdbcConnectionParameters = (JdbcConnectionParametersRequest) requestConnectionParameters;
                connectionParameters = new JdbcConnectionParameters(jdbcConnectionParameters.url, jdbcConnectionParameters.user, jdbcConnectionParameters.password);
                break;
            case XML:
                XMlConnectionParametersRequest xmlRepoConnectionParameters = (XMlConnectionParametersRequest) requestConnectionParameters;
                connectionParameters =  new XmlConnectionParameters(xmlRepoConnectionParameters.filename);
                break;
            case MOCK:
            default:
                connectionParameters =  new MockConnectionParameters();
                break;
        }
        RepoService repoService = this.repoServiceProvider.getService(requestConnectionParameters.repoType);
        repoService.connect(connectionParameters);

        return connectionParameters;
    }


    private List<ElementClass> readElementClasses(RepoType repoType) throws VBroAppException, RepoException {
        ElementClassService elementClassService = this.elementClassServiceProvider.getService(repoType);
        List<ElementClass> elementClasses = elementClassService.readElementClasses();
        List<String> elementClassNames = elementClasses
            .stream()
            .map(ElementClass::getName)
            .sorted()
            .collect(Collectors.toList());

        return elementClasses;
    }


    private RepoConnectionResponse getResponseRepoConnection(ConnectionParameters connectionParameters) {
        switch (connectionParameters.getType()) {
            case JDBC:
                JdbcConnectionParameters jdbcConnectionParameters = (JdbcConnectionParameters) connectionParameters;
                return new JdbcConnectionResponse(jdbcConnectionParameters.getUrl(), jdbcConnectionParameters.getUser(), jdbcConnectionParameters.getPassword());
            case XML:
                XmlConnectionParameters xmlConnectionParameters = (XmlConnectionParameters) connectionParameters;
                return new XmlConnectionResponse(xmlConnectionParameters.getFilename());
            case MOCK:
            default:
                return new MockConnectionResponse();
        }
    }


    private List<ElementClassResponse> getResponseElementClasses(List<ElementClass> elementClasses) {
        return elementClasses
            .stream()
            .map(elementClass -> new ElementClassResponse(elementClass.getName()))
            .collect(Collectors.toList());
    }
}
