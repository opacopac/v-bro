package com.tschanz.v_bro.versioning.usecase.read_versions;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.Date;


public interface ReadVersionsUseCase {
    ReadVersionsResponse readVersions(OpenConnectionResponse.RepoConnection repoConnection, String elementClassName, String elementId, Date minVon, Date maxBis) throws VBroAppException;
}
