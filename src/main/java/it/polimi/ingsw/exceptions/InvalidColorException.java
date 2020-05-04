package it.polimi.ingsw.exceptions;

public class InvalidColorException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid color exception";

    public InvalidColorException() {
        super(ERROR_MESSAGE);
    }
}
