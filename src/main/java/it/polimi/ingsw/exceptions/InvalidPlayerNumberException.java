package it.polimi.ingsw.exceptions;

public class InvalidPlayerNumberException extends RuntimeException{
    private final static String ERROR_MESSAGE = "Invalid Player Number given";

    public InvalidPlayerNumberException() {
        super(ERROR_MESSAGE);
    }
}
