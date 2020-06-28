package it.polimi.ingsw.exceptions;

/**
 * This exception is raised the user types a malformed command in CLI.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class BadCommandException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Bad Player Command";

    public BadCommandException() {
        super(ERROR_MESSAGE);
    }
}
