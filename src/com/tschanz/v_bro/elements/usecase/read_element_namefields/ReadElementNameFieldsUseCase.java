package com.tschanz.v_bro.elements.usecase.read_element_namefields;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;


public interface ReadElementNameFieldsUseCase {
    ReadElementNameFieldsResponse readNameFields(OpenConnectionResponse.RepoConnection repoConnection, String elementTableName) throws VBroAppException;
}
