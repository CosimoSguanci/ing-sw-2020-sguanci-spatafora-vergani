package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when an attempt to add a {@link it.polimi.ingsw.model.Player} to a full Match is made.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class AlreadyInsidePlayerException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Player already inside this match";

    public AlreadyInsidePlayerException() {
        super(ERROR_MESSAGE);
    }
}
