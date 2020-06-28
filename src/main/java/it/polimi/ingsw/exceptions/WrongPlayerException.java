package it.polimi.ingsw.exceptions;

/**
 * This exception is raised the a {@link it.polimi.ingsw.model.Player} sends a {@link it.polimi.ingsw.controller.commands.Command} when it's not its turn.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class WrongPlayerException extends RuntimeException {
    private final static String ERROR_MESSAGE = "This Player it's not the current Player for this turn";

    public WrongPlayerException() {
        super(ERROR_MESSAGE);
    }
}
