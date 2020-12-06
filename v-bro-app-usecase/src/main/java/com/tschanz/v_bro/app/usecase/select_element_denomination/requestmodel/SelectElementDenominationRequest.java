package com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class SelectElementDenominationRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final List<String> denominations;
}
