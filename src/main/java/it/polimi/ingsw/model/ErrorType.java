package it.polimi.ingsw.model;

/**
 * ErrorType is the enumeration representing the specific Error which just occurred (in case a command hasn't gone well).
 * <p>
 * DENIED_BY_OPPONENT_GOD: occurs when a command attempted cannot be performed due to another God's powers.
 * DENIED_BY_PLAYER_GOD: occurs when a command attempted cannot be performed because the Player's God doesn't offer this feature in this particular game context.
 * WRONG_TURN: the command couldn't be executed because it's another Player's turn.
 * WRONG_GAME_PHASE: the command couldn't be executed because it's not the correct command for the current Game Phase.
 * ALREADY_TAKEN_NICKNAME: the nickname passed in the command was already taken by another Player in the same match.
 * INVALID_COLOR: the color passed in the command was already taken by another Player in the same match, or it's an invalid color.
 * INVALID_GOD: the god passed in the command was already taken by another Player in the same match, or it's an invalid god.
 * INVALID_CELL: the cell passed in the command is not a valid cell.
 * GENERIC_ERROR: for some reason, the command could not be performed and failed.
 *
 * @author Cosimo Sguanci
 */
public enum ErrorType {
    DENIED_BY_OPPONENT_GOD,
    DENIED_BY_PLAYER_GOD,
    WRONG_TURN,
    WRONG_GAME_PHASE,
    ALREADY_TAKEN_NICKNAME,
    INVALID_COLOR,
    INVALID_GOD,
    INVALID_CELL,
    GENERIC_ERROR
}
