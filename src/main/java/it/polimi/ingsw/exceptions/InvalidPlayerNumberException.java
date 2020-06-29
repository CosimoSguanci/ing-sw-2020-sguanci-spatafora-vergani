package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when a client sends an invalid player number in the initial phase of the connection.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 * @see it.polimi.ingsw.network.server.Server#isValidPlayerNumber(int)
 */
public class InvalidPlayerNumberException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Invalid Player Number given";

    public InvalidPlayerNumberException() {
        super(ERROR_MESSAGE);
    }
}
