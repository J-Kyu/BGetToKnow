package com.kyu.BGetToKnowYou.exception;

public class NoneExistingRowException extends RuntimeException{

    public NoneExistingRowException() {
        super();
    }

    public NoneExistingRowException(String message) {
        super(message);
    }

    public NoneExistingRowException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoneExistingRowException(Throwable cause) {
        super(cause);
    }
}

