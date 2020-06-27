package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

/**
 * This class represents an update sent from Server to Client when the match actually starts.
 *
 * @author Cosimo Sguanci
 */
public class MatchStartedUpdate extends Update {

    public MatchStartedUpdate(String board) {
        super(board);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}