package it.polimi.ingsw.exceptions;

public class BadCommandException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Bad Player Command";

    public BadCommandException() {
        super(ERROR_MESSAGE);
    }
}
