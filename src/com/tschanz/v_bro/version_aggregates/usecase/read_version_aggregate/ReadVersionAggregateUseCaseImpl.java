package com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.version_aggregates.domain.model.AggregateNode;
import com.tschanz.v_bro.version_aggregates.domain.model.VersionAggregate;
import com.tschanz.v_bro.version_aggregates.domain.service.VersionAggregateService;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadVersionAggregateUseCaseImpl implements ReadVersionAggregateUseCase {
    private final Logger logger = Logger.getLogger(ReadVersionAggregateUseCaseImpl.class.getName());
    private final Map<RepoType, VersionAggregateService> versionAggregateServiceMap;


    public ReadVersionAggregateUseCaseImpl(Map<RepoType, VersionAggregateService> versionAggregateServiceMap) {
        this.versionAggregateServiceMap = versionAggregateServiceMap;
    }


    @Override
    public ReadVersionAggregateResponse readVersionAggregate(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementName,
        String elementId,
        String versionId
    ) throws VBroAppException {
        this.logger.info("UC: reading version aggregate...");

        VersionAggregateService versionAggregateService = this.getVersionAggregateService(repoConnection.repoType);
        try {
            VersionAggregate versionAggregate = versionAggregateService.readVersionAggregate(elementName, elementId, versionId);
            ReadVersionAggregateResponse response = this.createResponse(versionAggregate);
            this.logger.info("successfully read version aggregate");
            return response;
        } catch (RepoException exception) {
            String message = "error reading version aggregate: " + exception.getMessage();
            this.logger.info(message);
            throw new VBroAppException(message, exception);
        }
    }



    private VersionAggregateService getVersionAggregateService(RepoType repoType) throws VBroAppException {
        VersionAggregateService versionAggregateService = this.versionAggregateServiceMap.get(repoType);
        if (versionAggregateService == null) {
            String message = "no repo_data found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return versionAggregateService;
    }


    private ReadVersionAggregateResponse createResponse(VersionAggregate versionAggregate) {
        return new ReadVersionAggregateResponse(
            new ReadVersionAggregateResponse.VersionAggregateInfo(
                this.createAggregateNodeEntry(versionAggregate.getRootNode())
            )
        );
    }


    private ReadVersionAggregateResponse.AggregateNodeInfo createAggregateNodeEntry(AggregateNode aggregateNode) {
        return new ReadVersionAggregateResponse.AggregateNodeInfo(
            aggregateNode.getNodeName(),
            aggregateNode.getFieldValues(),
            aggregateNode.getChildNodes()
                .stream()
                .map(this::createAggregateNodeEntry)
                .collect(Collectors.toList())
        );
    }
}
