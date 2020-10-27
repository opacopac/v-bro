package com.tschanz.v_bro.element_classes.usecase.read_element_classes;

import com.tschanz.v_bro.common.VBroAppException;
import com.tschanz.v_bro.repo.usecase.open_connection.OpenConnectionResponse;


public interface ReadElementClassesUseCase {
    ReadElementClassesResponse readElementClasses(OpenConnectionResponse.RepoConnection repoConnection) throws VBroAppException;
}
