package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when a {@link it.polimi.ingsw.model.Player} tries to select a {@link it.polimi.ingsw.model.gods.GodStrategy}
 * which is not valid or because that {@link it.polimi.ingsw.model.gods.GodStrategy} has already been chosen, or it's not selectable for this match.
 * This can happen in {@link it.polimi.ingsw.controller.GamePhase#CHOOSE_GODS} game phase.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class InvalidGodException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid god chosen";

    public InvalidGodException() {
        super(ERROR_MESSAGE);
    }
}
