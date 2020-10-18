package com.tschanz.v_bro.common;


public class VBroAppException extends Throwable {
    private final Throwable innerException;
    private final String message;


    @Override
    public String getMessage() {
        return this.message;
    }


    public Throwable getInnerException() {
        return this.innerException;
    }


    public VBroAppException(String message) {
        this.message = message;
        this.innerException = null;
    }


    public VBroAppException(String message, Throwable innerException) {
        this.message = message;
        this.innerException = innerException;
    }
}
