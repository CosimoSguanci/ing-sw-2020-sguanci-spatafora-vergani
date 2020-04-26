package it.polimi.ingsw.exceptions;

public class WrongGamePhaseException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This is not the right game phase for this command";

    public WrongGamePhaseException() {
        super(ERROR_MESSAGE);
    }
}