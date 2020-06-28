package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when a {@link it.polimi.ingsw.model.Player} tries to select a {@link it.polimi.ingsw.model.gods.GodStrategy}
 * which is not valid or because that {@link it.polimi.ingsw.model.gods.GodStrategy} has a String representation which cannot be recognized as valid.
 * This can happen in {@link it.polimi.ingsw.controller.GamePhase#CHOOSE_GODS} game phase.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class UnknownGodException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Unknown God";

    public UnknownGodException() {
        super(ERROR_MESSAGE);
    }
}