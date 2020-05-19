package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class MatchStartedUpdate extends Update {

    public MatchStartedUpdate(String board) {
        super(null, board);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
