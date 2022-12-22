package com.kyu.BGetToKnowYou.exception;

public class NoRoomTicketFoundException extends RuntimeException{

    public NoRoomTicketFoundException() {
        super();
    }

    public NoRoomTicketFoundException(String message) {
        super(message);
    }

    public NoRoomTicketFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRoomTicketFoundException(Throwable cause) {
        super(cause);
    }

}
