package com.tschanz.v_bro.repo.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RepoException extends Exception {
    @Getter private final Exception innerException;


    public RepoException(String message) {
        this(message, (Exception) null);
    }


    public RepoException(String message, Exception innerException) {
        super(message);
        this.innerException = innerException;
    }
}
