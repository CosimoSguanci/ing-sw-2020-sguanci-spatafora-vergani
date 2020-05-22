package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

public class ServerUnreachableUpdate extends Update {
    public ServerUnreachableUpdate() {
        super( null);
    }

    @Override
    public void handleUpdate(UpdateHandler handler) {
        handler.handle(this);
    }
}
