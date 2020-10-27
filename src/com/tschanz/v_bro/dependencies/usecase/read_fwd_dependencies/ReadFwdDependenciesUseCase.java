package com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;


public interface ReadFwdDependenciesUseCase {
    ReadFwdDependenciesResponse readFwdDependencies(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementName,
        String elementId,
        String versionId
    ) throws VBroAppException;
}
