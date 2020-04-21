package it.polimi.ingsw.exceptions;

public class WrongPlayerException extends RuntimeException{
    private final static String ERROR_MESSAGE = "This Player it's not the current Player for this turn";

    public WrongPlayerException() {
        super(ERROR_MESSAGE);
    }
}
