package it.polimi.ingsw.exceptions;

public class AlreadyInsidePlayerException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Player already inside this match";

    public AlreadyInsidePlayerException() {
        super(ERROR_MESSAGE);
    }
}
