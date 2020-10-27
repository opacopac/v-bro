package com.tschanz.v_bro.element_classes.usecase.read_element_denominations;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;


public interface ReadElementDenominationsUseCase {
    ReadElementDenominationsResponse readDenominations(OpenConnectionResponse.RepoConnection repoConnection, String elementTableName) throws VBroAppException;
}
