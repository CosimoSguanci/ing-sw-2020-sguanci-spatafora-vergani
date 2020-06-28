package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when an action on a {@link it.polimi.ingsw.model.Cell} which is not empty is attempted.
 * For example, when a {@link it.polimi.ingsw.model.Worker} tries to move to a {@link it.polimi.ingsw.model.Cell} already occupied by
 * another {@link it.polimi.ingsw.model.Worker}
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class CellNotEmptyException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This Cell it's not empty";

    public CellNotEmptyException() {
        super(ERROR_MESSAGE);
    }
}
