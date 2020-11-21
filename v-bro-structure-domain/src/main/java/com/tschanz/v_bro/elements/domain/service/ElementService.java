package com.tschanz.v_bro.elements.domain.service;

import com.tschanz.v_bro.elements.domain.model.ElementData;
import com.tschanz.v_bro.repo.domain.model.RepoException;

import java.util.Collection;
import java.util.List;


public interface ElementService {
    List<ElementData> readElements(String elementClass, Collection<String> fieldNames) throws RepoException;
}
