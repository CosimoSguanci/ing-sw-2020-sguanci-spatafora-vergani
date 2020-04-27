package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class TurnUpdate extends PlayerSpecificUpdate {
    public TurnUpdate(String playerID) {
        super(playerID);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
