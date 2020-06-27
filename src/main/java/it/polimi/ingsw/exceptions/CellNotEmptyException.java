package it.polimi.ingsw.exceptions;

public class CellNotEmptyException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This Cell it's not empty";

    public CellNotEmptyException() {
        super(ERROR_MESSAGE);
    }
}
