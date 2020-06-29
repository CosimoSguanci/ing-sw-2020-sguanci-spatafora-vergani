package it.polimi.ingsw.model;

/**
 * ErrorType is the enumeration representing the specific Error which just occurred (in case a command hasn't gone well).
 * <p>
 *
 * @author Cosimo Sguanci
 */
public enum ErrorType {

    /**
     * Occurs when a command attempted cannot be performed due to another God's powers.
     */
    DENIED_BY_OPPONENT_GOD,

    /**
     * Occurs when a command attempted cannot be performed because the {@link Player}'s God doesn't offer this feature in this particular game context.
     */
    DENIED_BY_PLAYER_GOD,

    /**
     * The command couldn't be executed because it's another {@link Player}'s turn.
     */
    WRONG_TURN,

    /**
     * The command couldn't be executed because it's not the correct command for the current {@link it.polimi.ingsw.controller.GamePhase}.
     */
    WRONG_GAME_PHASE,

    /**
     * The nickname passed in the command was already taken by another {@link Player} in the same match.
     */
    ALREADY_TAKEN_NICKNAME,

    /**
     * The {@link PrintableColor} passed in the command was already taken by another {@link Player} in the same match, or it's an invalid {@link PrintableColor}.
     */
    INVALID_COLOR,

    /**
     * The god passed in the command was already taken by another {@link Player} in the same match, or it's an invalid god.
     */
    INVALID_GOD,

    /**
     * The {@link Cell} passed in the command is not a valid {@link Cell}.
     */
    INVALID_CELL,

    /**
     * For some reason, the command could not be performed and failed.
     */
    GENERIC_ERROR
}
