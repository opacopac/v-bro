package com.tschanz.v_bro.versioning.usecase.read_version_timeline;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;
import com.tschanz.v_bro.versioning.domain.model.Pflegestatus;

import java.time.LocalDate;


public interface ReadVersionTimelineUseCase {
    ReadVersionTimelineResponse readVersionTimeline(
        OpenConnectionResponse.RepoConnection repoConnection,
        String elementClassName,
        String elementId,
        LocalDate minVon,
        LocalDate maxBis,
        Pflegestatus minPflegestatus
    ) throws VBroAppException;
}
