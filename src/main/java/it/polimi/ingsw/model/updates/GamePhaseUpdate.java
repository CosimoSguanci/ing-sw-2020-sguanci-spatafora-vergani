package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.controller.GamePhase;
import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client every the Game Phase changes.
 *
 * @author Cosimo Sguanci
 */
public class GamePhaseUpdate extends Update {
    public final GamePhase newGamePhase;

    public GamePhaseUpdate(GamePhase newGamePhase) {
        super( null);
        this.newGamePhase = newGamePhase;
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
