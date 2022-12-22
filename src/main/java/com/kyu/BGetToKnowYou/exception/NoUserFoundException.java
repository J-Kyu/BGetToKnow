package com.kyu.BGetToKnowYou.exception;

public class NoUserFoundException extends RuntimeException{


    public NoUserFoundException() {
        super();
    }

    public NoUserFoundException(String message) {
        super(message);
    }

    public NoUserFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUserFoundException(Throwable cause) {
        super(cause);
    }


}
