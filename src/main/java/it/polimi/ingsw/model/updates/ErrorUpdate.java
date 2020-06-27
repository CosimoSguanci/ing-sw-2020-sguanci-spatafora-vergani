package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.CommandType;
import it.polimi.ingsw.model.ErrorType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

import java.util.Map;

/**
 * This class represents an update sent from Server to Client every time an error occurs.
 * For example, this kind of Update can be generated and sent when a Worker attempts to move to a position to which it is not adjacent.
 *
 * @author Cosimo Sguanci
 */
public class ErrorUpdate extends Update {
    private static final long serialVersionUID = -6803649711188401133L;

    /**
     * The command type which caused the error
     */
    public final CommandType command;

    public final ErrorType errorType;

    /**
     * [Optional] God who forbids the action attempted
     */
    private final Map<String, String> inhibitorGod;

    /**
     * The Player who made the command which caused the error
     */
    private final Player currentPlayer;

    public ErrorUpdate(Player currentPlayer, CommandType command, ErrorType errorType, Map<String, String> inhibitorGod) {
        super(null);
        this.currentPlayer = currentPlayer;
        this.command = command;
        this.errorType = errorType;
        this.inhibitorGod = inhibitorGod;
    }

    /**
     * Current player getter
     * @return the Player who caused the error
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Inhibitor God getter
     * @return String representation of the gods which denied the attempted command
     */
    public Map<String, String> getInhibitorGod() {
        return this.inhibitorGod;
    }

    /**
     * Utility method used to implement Visitor Pattern for Updates handling.
     * @see UpdateHandler
     * @param handler which handle the updates to update the View
     */
    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
