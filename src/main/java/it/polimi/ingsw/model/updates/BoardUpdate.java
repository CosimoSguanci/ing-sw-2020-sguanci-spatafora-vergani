package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class BoardUpdate extends BroadcastUpdate {

    public BoardUpdate(String board) {
        super(board);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
