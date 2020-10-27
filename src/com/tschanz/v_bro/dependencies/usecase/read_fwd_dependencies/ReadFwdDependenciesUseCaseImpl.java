package com.tschanz.v_bro.dependencies.usecase.read_fwd_dependencies;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.dependencies.domain.service.DependencyService;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.dependencies.domain.model.FwdDependency;
import com.tschanz.v_bro.versions.domain.model.VersionInfo;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadFwdDependenciesUseCaseImpl implements ReadFwdDependenciesUseCase {
    private final Logger logger = Logger.getLogger(ReadFwdDependenciesUseCaseImpl.class.getName());
    private final Map<RepoType, DependencyService> dependencyServiceMap;


    public ReadFwdDependenciesUseCaseImpl(Map<RepoType, DependencyService> dependencyServiceMap) {
        this.dependencyServiceMap = dependencyServiceMap;
    }


    @Override
    public ReadFwdDependenciesResponse readFwdDependencies(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementName,
        String elementId,
        String versionId
    ) throws VBroAppException {
        this.logger.info("UC: reading fwd dependencies...");

        DependencyService dependencyService = this.getDependencyService(repoConnection.repoType);
        try {
            Collection<FwdDependency> fwdDependencies = dependencyService.readFwdDependencies(elementName, elementId, versionId);
            ReadFwdDependenciesResponse response = new ReadFwdDependenciesResponse(
                fwdDependencies
                    .stream()
                    .map(fwdDep -> new ReadFwdDependenciesResponse.FwdDependencyInfo(
                        fwdDep.elementName(),
                        fwdDep.elementId(),
                        fwdDep.getVersions()
                            .stream()
                            .map(version -> new VersionInfo(
                                version.getId(),
                                version.getGueltigVon(),
                                version.getGueltigBis(),
                                version.getPflegestatus()
                            ))
                            .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList())
            );
            this.logger.info("successfully read " + response.fwdDependencies.size() + " fwd dependencies");
            return response;
        } catch (RepoException exception) {
            String message = "error reading fwd dependencies: " + exception.getMessage();
            this.logger.info(message);
            throw new VBroAppException(message, exception);
        }
    }


    private DependencyService getDependencyService(RepoType repoType) throws VBroAppException {
        DependencyService dependencyService = this.dependencyServiceMap.get(repoType);
        if (dependencyService == null) {
            String message = "no repo_data found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return dependencyService;
    }
}
