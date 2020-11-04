package com.tschanz.v_bro.app.usecase.disconnect_repo;

import com.tschanz.v_bro.app.usecase.disconnect_repo.requestmodel.CloseConnectionRequest;


public interface CloseConnectionUseCase {
    void execute(CloseConnectionRequest request);
}
