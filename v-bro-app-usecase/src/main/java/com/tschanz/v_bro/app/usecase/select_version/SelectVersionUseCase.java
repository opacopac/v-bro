package com.tschanz.v_bro.app.usecase.select_version;


import com.tschanz.v_bro.app.usecase.select_version.requestmodel.SelectVersionRequest;

public interface SelectVersionUseCase {
    void execute(SelectVersionRequest request);
}
