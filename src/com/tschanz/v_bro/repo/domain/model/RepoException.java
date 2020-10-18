package com.tschanz.v_bro.repo.domain.model;


public class RepoException extends Exception {
    private final Exception innerException;


    public Exception getInnerException() { return innerException; }


    public RepoException(Exception exception) {
        this(exception.getMessage(), exception);
    }


    public RepoException(String message) {
        this(message, null);
    }


    public RepoException(String message, Exception innerException) {
        super(message);
        this.innerException = innerException;
    }
}
