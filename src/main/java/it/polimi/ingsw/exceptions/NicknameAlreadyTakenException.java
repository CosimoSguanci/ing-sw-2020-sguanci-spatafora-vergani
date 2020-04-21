package it.polimi.ingsw.exceptions;

public class NicknameAlreadyTakenException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Nickname already taken for this match";

    public NicknameAlreadyTakenException() {
        super(ERROR_MESSAGE);
    }
}
