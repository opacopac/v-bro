package com.tschanz.v_bro.app.usecase.connect_repo;


import com.tschanz.v_bro.app.usecase.connect_repo.requestmodel.OpenConnectionRequest;

public interface OpenConnectionUseCase {
    void execute(OpenConnectionRequest request);
}
