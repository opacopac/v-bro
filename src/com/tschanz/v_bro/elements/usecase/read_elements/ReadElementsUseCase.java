package com.tschanz.v_bro.elements.usecase.read_elements;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;

import java.util.List;


public interface ReadElementsUseCase {
    ReadElementsResponse readElements(OpenConnectionResponse.RepoConnection repoConnection, String elementTableName, List<String> nameFields) throws VBroAppException;
}
