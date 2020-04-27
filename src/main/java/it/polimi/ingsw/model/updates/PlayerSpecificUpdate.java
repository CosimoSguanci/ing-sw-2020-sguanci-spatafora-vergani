package it.polimi.ingsw.model.updates;

import java.io.Serializable;

public abstract class PlayerSpecificUpdate extends Update implements Serializable {
    public final String playerID;

    PlayerSpecificUpdate(String playerID) {
        this.playerID = playerID;
    }
}
