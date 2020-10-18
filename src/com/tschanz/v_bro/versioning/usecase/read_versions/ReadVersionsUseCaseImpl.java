package com.tschanz.v_bro.versioning.usecase.read_versions;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versioning.domain.model.VersionData;
import com.tschanz.v_bro.versioning.domain.service.VersionService;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadVersionsUseCaseImpl implements ReadVersionsUseCase {
    private final Logger logger = Logger.getLogger(ReadVersionsUseCaseImpl.class.getName());
    private final Map<RepoType, VersionService> versionDataServiceMap;


    public ReadVersionsUseCaseImpl(Map<RepoType, VersionService> versionDataServiceMap) {
        this.versionDataServiceMap = versionDataServiceMap;
    }


    @Override
    public ReadVersionsResponse readVersions(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementClassName,
        String elementId,
        Date minVon,
        Date maxBis
    ) throws VBroAppException {
        this.logger.info("UC: reading versions...");

        VersionService versionService = this.getVersionDataService(repoConnection.repoType);
        try {
            Collection<VersionData> versionList = versionService.readVersions(elementClassName, elementId);
            ReadVersionsResponse response = new ReadVersionsResponse(
                versionList
                    .stream()
                    .map(version -> new ReadVersionsResponse.Version(
                        version.getId(),
                        version.getGueltigVon(),
                        version.getGueltigBis())
                    )
                    .collect(Collectors.toList())
            );

            this.logger.info("successfully read " + response.versions.size() + " versions");

            return response;
        } catch (RepoException exception) {
            String message = "error reading versions: " + exception.getMessage();
            this.logger.info(message);
            throw new VBroAppException(message, exception);
        }
    }


    private VersionService getVersionDataService(RepoType repoType) throws VBroAppException {
        VersionService versionService = this.versionDataServiceMap.get(repoType);
        if (versionService == null) {
            String message = "no service found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return versionService;
    }
}
