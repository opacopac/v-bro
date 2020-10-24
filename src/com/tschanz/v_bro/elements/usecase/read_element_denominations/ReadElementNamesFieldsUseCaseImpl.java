package com.tschanz.v_bro.elements.usecase.read_element_denominations;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.elements.domain.model.Denomination;
import com.tschanz.v_bro.elements.domain.service.ElementService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadElementNamesFieldsUseCaseImpl implements ReadElementDenominationsUseCase {
    private final Logger logger = Logger.getLogger(ReadElementNamesFieldsUseCaseImpl.class.getName());
    private final Map<RepoType, ElementService> elementServiceMap;


    public ReadElementNamesFieldsUseCaseImpl(Map<RepoType, ElementService> elementServiceMap) {
        this.elementServiceMap = elementServiceMap;
    }


    @Override
    public ReadElementDenominationsResponse readDenominations(OpenConnectionResponse.RepoConnection repoConnection, String elementTableName) throws VBroAppException {
        this.logger.info("UC: reading element denominations...");

        ElementService elementService = this.getElementService(repoConnection.repoType);
        try {
            Collection<Denomination> denominations = elementService.readDenominations(elementTableName);
            ReadElementDenominationsResponse response = new ReadElementDenominationsResponse(
                denominations
                    .stream()
                    .map(denomination -> new ReadElementDenominationsResponse.ElementDenomination(denomination.getName()))
                    .collect(Collectors.toList())
            );

            this.logger.info("successfully read " + response.denominations.size() + " element denominations");

            return response;
        } catch (RepoException exception) {
            String message = "error reading element denominations: " + exception.getMessage();
            this.logger.severe(message);
            throw new VBroAppException(message, exception);
        }
    }


    private ElementService getElementService(RepoType repoType) throws VBroAppException {
        ElementService elementService = this.elementServiceMap.get(repoType);
        if (elementService == null) {
            String message = "no repo_data found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return elementService;
    }
}

