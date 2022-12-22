package com.kyu.BGetToKnowYou.exception;

public class NoRoomFoundException extends RuntimeException{


    public NoRoomFoundException() {
        super();
    }

    public NoRoomFoundException(String message) {
        super(message);
    }

    public NoRoomFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRoomFoundException(Throwable cause) {
        super(cause);
    }
}
