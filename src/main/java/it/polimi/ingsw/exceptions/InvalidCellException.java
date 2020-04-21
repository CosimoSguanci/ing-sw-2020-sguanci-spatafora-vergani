package it.polimi.ingsw.exceptions;

public class InvalidCellException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid Cell used";

    public InvalidCellException() {
        super(ERROR_MESSAGE);
    }
}
