package com.tschanz.v_bro.elements.domain.service;

import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;


public interface ElementService {
    Collection<ElementData> readElements(String elementClass, Collection<String> fieldNames) throws RepoException;
}
