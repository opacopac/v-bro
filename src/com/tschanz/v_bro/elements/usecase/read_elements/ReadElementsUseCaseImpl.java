package com.tschanz.v_bro.elements.usecase.read_elements;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadElementsUseCaseImpl implements ReadElementsUseCase {
    private final Logger logger = Logger.getLogger(ReadElementsUseCaseImpl.class.getName());
    private final Map<RepoType, ElementService> elementServiceMap;


    public ReadElementsUseCaseImpl(Map<RepoType, ElementService> elementServiceMap) {
        this.elementServiceMap = elementServiceMap;
    }


    @Override
    public ReadElementsResponse readElements(OpenConnectionResponse.RepoConnection repoConnection, String elementTableName, List<String> nameFields) throws VBroAppException {
        this.logger.info("UC: reading elements...");

        ElementService elementDataService = this.getElementDataService(repoConnection.repoType);
        try {
            Collection<ElementData> elements = elementDataService.readElements(elementTableName, nameFields);
            ReadElementsResponse response = new ReadElementsResponse(
                elements
                    .stream()
                    .map(element -> new ReadElementsResponse.Element(element.getId(), element.getId())) // TODO => name fields
                    .collect(Collectors.toList())
            );

            this.logger.info("successfully read " + response.elements.size() + " elements");

            return response;
        } catch (RepoException exception) {
            String message = "error reading elements: " + exception.getMessage();
            this.logger.severe(message);
            throw new VBroAppException(message, exception);
        }
    }


    private ElementService getElementDataService(RepoType repoType) throws VBroAppException {
        ElementService elementService = this.elementServiceMap.get(repoType);
        if (elementService == null) {
            String message = "no repo_data found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return elementService;
    }
}

