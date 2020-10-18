package com.tschanz.v_bro.repo.usecase.close_connection;

import com.tschanz.v_bro.common.VBroAppException;


public interface CloseConnectionUseCase {
    void disconnect() throws VBroAppException;
}
