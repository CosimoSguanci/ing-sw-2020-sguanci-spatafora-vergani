package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.commands.PlayerCommand;
import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client every time the Match Board changes (for example, after a move/build, or after a Player lost)
 *
 * @author Cosimo Sguanci
 */
public class BoardUpdate extends Update {
    /**
     * This attributes is used to keep track of the last command made, which is the command that changed the state (and caused the update to be generated and sent)
     */
    private PlayerCommand executedCommand;

    public BoardUpdate(String board) {
        super(board);
    }

    /**
     * Last executed command setter
     * @param executedCommand last executed command
     */
    public void setExecutedCommand(PlayerCommand executedCommand) {
        this.executedCommand = executedCommand;
    }

    /**
     * Last executed command getter
     * @return last executed command
     */
    public PlayerCommand getExecutedCommand() {
        return this.executedCommand;
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
