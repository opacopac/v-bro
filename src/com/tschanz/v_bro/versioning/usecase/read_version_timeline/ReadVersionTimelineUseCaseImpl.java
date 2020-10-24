package com.tschanz.v_bro.versioning.usecase.read_version_timeline;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versioning.domain.model.Pflegestatus;
import com.tschanz.v_bro.versioning.domain.model.VersionInfo;
import com.tschanz.v_bro.versioning.domain.service.VersionService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class ReadVersionTimelineUseCaseImpl implements ReadVersionTimelineUseCase {
    private final Logger logger = Logger.getLogger(ReadVersionTimelineUseCaseImpl.class.getName());
    private final Map<RepoType, VersionService> versionDataServiceMap;


    public ReadVersionTimelineUseCaseImpl(Map<RepoType, VersionService> versionDataServiceMap) {
        this.versionDataServiceMap = versionDataServiceMap;
    }


    @Override
    public ReadVersionTimelineResponse readVersionTimeline(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementClassName,
        String elementId,
        LocalDate minVon,
        LocalDate maxBis,
        Pflegestatus minPflegestatus // TODO
    ) throws VBroAppException {
        this.logger.info("UC: reading versions...");

        VersionService versionService = this.getVersionDataService(repoConnection.repoType);
        try {
            Collection<VersionInfo> versionList = versionService.readVersionTimeline(elementClassName, elementId);
            ReadVersionTimelineResponse response = new ReadVersionTimelineResponse(
                versionList
                    .stream()
                    .map(version -> new ReadVersionTimelineResponse.VersionInfoEntry(
                        version.getId(),
                        version.getGueltigVon(),
                        version.getGueltigBis())
                    )
                    .collect(Collectors.toList())
            );

            this.logger.info("successfully read " + response.versionInfoEntries.size() + " versions");

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
            String message = "no repo_data found for type " + repoType.name();
            this.logger.severe(message);
            throw new VBroAppException(message);
        }

        return versionService;
    }
}
