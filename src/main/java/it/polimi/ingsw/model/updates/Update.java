package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

public abstract class Update implements Serializable {
    public abstract void handleUpdate(UpdateHandler handler);
}
