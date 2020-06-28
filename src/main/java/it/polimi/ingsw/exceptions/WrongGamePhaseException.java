package it.polimi.ingsw.exceptions;

/**
 * This exception is raised the a client sends a {@link it.polimi.ingsw.controller.commands.Command} which is not performable
 * in the current {@link it.polimi.ingsw.controller.GamePhase} of this match.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class WrongGamePhaseException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This is not the right game phase for this command";

    public WrongGamePhaseException() {
        super(ERROR_MESSAGE);
    }
}