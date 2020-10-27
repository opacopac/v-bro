package com.tschanz.v_bro.element_classes.domain.service;

import com.tschanz.v_bro.element_classes.domain.model.ElementClass;
import com.tschanz.v_bro.element_classes.domain.model.Denomination;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public interface ElementClassService {
    Collection<ElementClass> readElementClasses() throws RepoException;

    List<Denomination> readDenominations(String elementName) throws RepoException;
}
