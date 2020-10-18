package com.tschanz.v_bro.elements.domain.service;

import com.tschanz.v_bro.elements.domain.model.ElementClass;
import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.elements.domain.model.NameField;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public interface ElementService {
    Collection<ElementClass> readElementClasses() throws RepoException;

    List<NameField> readNameFields(String elementName) throws RepoException;

    Collection<ElementData> readElementData(String elementName, Collection<String> fieldNames) throws RepoException;
}
