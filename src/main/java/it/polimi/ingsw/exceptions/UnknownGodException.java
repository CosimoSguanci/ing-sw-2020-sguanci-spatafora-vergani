package it.polimi.ingsw.exceptions;

public class UnknownGodException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Unknown God";

    public UnknownGodException() {
        super(ERROR_MESSAGE);
    }
}