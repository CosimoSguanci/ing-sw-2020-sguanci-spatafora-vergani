package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when an action on a {@link it.polimi.ingsw.model.Cell} which is not valid is attempted.
 * For example, when a client generates a {@link it.polimi.ingsw.controller.commands.Command} which contains invalid
 * rows/cols values (a {@link it.polimi.ingsw.controller.commands.CommandType#MOVE} to a {@link it.polimi.ingsw.model.Cell}
 * with row x and col y, with x greater than 4 and/or y greater than 4).
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class InvalidCellException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid Cell used";

    public InvalidCellException() {
        super(ERROR_MESSAGE);
    }
}
