package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class PlayerUpdate extends PlayerSpecificUpdate { // TODO Consider Removing

    public PlayerUpdate(String playerID) {
        super(playerID);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }

}
