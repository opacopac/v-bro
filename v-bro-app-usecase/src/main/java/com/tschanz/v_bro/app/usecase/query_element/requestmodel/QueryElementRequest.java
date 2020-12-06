package com.tschanz.v_bro.app.usecase.query_element.requestmodel;

import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class QueryElementRequest {
    public final RepoType repoType;
    public final String elementClass;
    public final List<String> selectedDenominations;
    public final String elementQuery;
    public final long requestTimestamp;
}
