package com.tschanz.v_bro.app.usecase.select_element_denomination.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;

import java.util.List;


public class SelectElementDenominationRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final List<String> denominations;


    public SelectElementDenominationRequest(
        RepoType repoType,
        String elementClass,
        List<String> denominations
    ) {
        this.repoType = repoType;
        this.elementClass = elementClass;
        this.denominations = denominations;
    }
}
