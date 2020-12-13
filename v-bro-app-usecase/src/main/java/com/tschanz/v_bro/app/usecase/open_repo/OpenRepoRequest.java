package com.tschanz.v_bro.app.usecase.open_repo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class OpenRepoRequest {
    private final ConnectionParametersRequest connectionParameters;
}
