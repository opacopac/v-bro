package com.tschanz.v_bro.app.usecase.open_dependency_version;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class OpenDependencyVersionRequest {
    private final String elementClass;
    private final String elementId;
    private final String versionId;
}
