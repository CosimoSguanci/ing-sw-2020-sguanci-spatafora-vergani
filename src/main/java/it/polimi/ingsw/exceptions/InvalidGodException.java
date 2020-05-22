package it.polimi.ingsw.exceptions;

public class InvalidGodException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid god chosen";

    public InvalidGodException() {
        super(ERROR_MESSAGE);
    }
}
