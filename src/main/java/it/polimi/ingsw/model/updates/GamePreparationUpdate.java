package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class GamePreparationUpdate extends Update {

    public GamePreparationUpdate(String playerID, String board) {
        super(playerID, board);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
