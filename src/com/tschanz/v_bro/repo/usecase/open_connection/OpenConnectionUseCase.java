package com.tschanz.v_bro.repo.usecase.open_connection;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.domain.model.ConnectionParameters;


public interface OpenConnectionUseCase {
    OpenConnectionResponse connect(ConnectionParameters connectionParameters)  throws VBroAppException;
}
