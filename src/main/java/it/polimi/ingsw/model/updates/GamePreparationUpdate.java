package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class GamePreparationUpdate extends PlayerSpecificUpdate {
    public final String board;

    public GamePreparationUpdate(String playerID, String board) {
        super(playerID);
        this.board = board;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
