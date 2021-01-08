package com.tschanz.v_bro.app.usecase.open_version;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class OpenVersionRequest {
    private final String versionId;
    private final boolean appendToHistory;
}
