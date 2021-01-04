package com.tschanz.v_bro.app.service;

import com.tschanz.v_bro.repo.domain.model.RepoException;
import com.tschanz.v_bro.repo.domain.model.RepoType;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;


@Log
public class RepoServiceProvider<T> {
    private final Map<RepoType, T> serviceMap = new HashMap<>();


    public RepoServiceProvider(RepoType repoType1, T service1, RepoType repoType2, T service2, RepoType repoType3, T service3) {
        if (repoType1 == repoType2 || repoType1 == repoType3 || repoType2 == repoType3) {
            throw new IllegalArgumentException("all repo types must be distinct");
        }

        this.serviceMap.put(repoType1, service1);
        this.serviceMap.put(repoType2, service2);
        this.serviceMap.put(repoType3, service3);
    }


    public T getService(RepoType repoType) throws RepoException {
        T service = this.serviceMap.get(repoType);

        if (service != null) {
            return service;
        } else {
            String message = "no service found for repo type " + repoType.name();
            log.severe(message);
            throw new RepoException(message);
        }
    }
}
