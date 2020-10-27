package com.tschanz.v_bro.element_classes.usecase.read_element_classes;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadElementClassesUseCaseImpl implements ReadElementClassesUseCase {
    private final Logger logger = Logger.getLogger(ReadElementClassesUseCaseImpl.class.getName());
    private final Map<RepoType, ElementClassService> elementClassServiceMap;


    public ReadElementClassesUseCaseImpl(Map<RepoType, ElementClassService> elementClassServiceMap) {
        this.elementClassServiceMap = elementClassServiceMap;
    }


    public ReadElementClassesResponse readElementClasses(OpenConnectionResponse.RepoConnection repoConnection) throws VBroAppException {
        this.logger.info("UC: reading element classes...");

        ElementClassService elementClassService = this.getElementClassService(repoConnection.repoType);

        try {
            Collection<ElementClass> elementClasses = elementClassService.readElementClasses();
            ReadElementClassesResponse response = new ReadElementClassesResponse(
                elementClasses
                .stream()
                .map(ElementClass::getName)
                .collect(Collectors.toList())
            );

            this.logger.info("successfully read " + response.elementTableNames.size() + " element classes");

            return response;
        } catch (RepoException exception) {
            String message = "error reading element classes: " + exception.getMessage();
            this.logger.severe(message);
            throw new VBroAppException(message, exception);
        }
    }


    private ElementClassService getElementClassService(RepoType repoType) throws VBroAppException {
        ElementClassService elementClassService = this.elementClassServiceMap.get(repoType);
        if (elementClassService == null) {
            String message = "no repo_data found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return elementClassService;
    }
}
