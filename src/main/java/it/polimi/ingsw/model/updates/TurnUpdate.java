package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client every time the current turn changes.
 *
 * @author Cosimo Sguanci
 */
public class TurnUpdate extends Update {

    /**
     * New current player
     */
    private final Player currentPlayer;

    public TurnUpdate(Player currentPlayer) {
        super(null);
        this.currentPlayer = currentPlayer;
    }

    /**
     * New current player getter
     * @return the new current player
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
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
