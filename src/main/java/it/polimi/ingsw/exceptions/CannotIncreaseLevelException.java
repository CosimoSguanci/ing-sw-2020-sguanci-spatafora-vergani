package it.polimi.ingsw.exceptions;

public class CannotIncreaseLevelException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This Cell's level is at its max";

    public CannotIncreaseLevelException() {
        super(ERROR_MESSAGE);
    }
}
