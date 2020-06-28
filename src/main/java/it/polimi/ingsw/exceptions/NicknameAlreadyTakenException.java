package it.polimi.ingsw.exceptions;

/**
 * This exception is raised when a {@link it.polimi.ingsw.model.Player} tries to select a nickname
 * which is not valid because that nickname has already been chosen. This can happen in {@link it.polimi.ingsw.controller.GamePhase#INITIAL_INFO} game phase.
 *
 * @author Cosimo Sguanci
 * @author Roberto Spatafora
 * @author Andrea Mario Vergani
 */
public class NicknameAlreadyTakenException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Nickname already taken for this match";

    public NicknameAlreadyTakenException() {
        super(ERROR_MESSAGE);
    }
}
