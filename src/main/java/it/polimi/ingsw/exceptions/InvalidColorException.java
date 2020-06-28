package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when a {@link it.polimi.ingsw.model.Player} tries to select a {@link it.polimi.ingsw.model.PrintableColor}
 * which is not valid, because its String representation is not recognized, or because that {@link it.polimi.ingsw.model.PrintableColor}
 * has already been chosen. This can happen in {@link it.polimi.ingsw.controller.GamePhase#INITIAL_INFO} game phase.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class InvalidColorException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid color exception";

    public InvalidColorException() {
        super(ERROR_MESSAGE);
    }
}
