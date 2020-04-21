package it.polimi.ingsw.exceptions;

public class BadPlayerCommandException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Bad Player Command";

    public BadPlayerCommandException() {
        super(ERROR_MESSAGE);
    }
}
