package it.polimi.ingsw.exceptions;

public class FullMatchException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This match is full";

    public FullMatchException() {
        super(ERROR_MESSAGE);
    }
}
