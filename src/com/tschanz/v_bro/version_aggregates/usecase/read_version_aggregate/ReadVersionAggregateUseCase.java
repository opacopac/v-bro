package com.tschanz.v_bro.version_aggregates.usecase.read_version_aggregate;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;


public interface ReadVersionAggregateUseCase {
    ReadVersionAggregateResponse readVersionAggregate(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementName,
        String elementId,
        String versionId
    ) throws VBroAppException;
}
