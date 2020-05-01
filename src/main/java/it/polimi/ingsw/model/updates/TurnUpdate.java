package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class TurnUpdate extends BroadcastUpdate {
    public final String currentPlayerID;

    public TurnUpdate(String playerID) {
        super(null);
        this.currentPlayerID = playerID;
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
