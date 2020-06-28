package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when a {@link it.polimi.ingsw.model.Worker} tries to build to a Cell which is already at the Dome level.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class CannotIncreaseLevelException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This Cell's level is at its max";

    public CannotIncreaseLevelException() {
        super(ERROR_MESSAGE);
    }
}
