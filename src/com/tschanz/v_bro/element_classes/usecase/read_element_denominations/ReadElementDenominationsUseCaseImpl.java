package com.tschanz.v_bro.element_classes.usecase.read_element_denominations;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.element_classes.domain.service.ElementClassService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadElementDenominationsUseCaseImpl implements ReadElementDenominationsUseCase {
    private final Logger logger = Logger.getLogger(ReadElementDenominationsUseCaseImpl.class.getName());
    private final Map<RepoType, ElementClassService> elementClassServiceMap;


    public ReadElementDenominationsUseCaseImpl(Map<RepoType, ElementClassService> elementClassServiceMap) {
        this.elementClassServiceMap = elementClassServiceMap;
    }


    @Override
    public ReadElementDenominationsResponse readDenominations(OpenConnectionResponse.RepoConnection repoConnection, String elementTableName) throws VBroAppException {
        this.logger.info("UC: reading element denominations...");

        ElementClassService elementClassService = this.getElementClassService(repoConnection.repoType);
        try {
            Collection<Denomination> denominations = elementClassService.readDenominations(elementTableName);
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

