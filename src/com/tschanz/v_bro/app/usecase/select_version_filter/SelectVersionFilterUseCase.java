package com.tschanz.v_bro.app.usecase.select_version_filter;


import com.tschanz.v_bro.app.usecase.select_version_filter.requestmodel.SelectVersionFilterRequest;

public interface SelectVersionFilterUseCase {
    void execute(SelectVersionFilterRequest request);
}
