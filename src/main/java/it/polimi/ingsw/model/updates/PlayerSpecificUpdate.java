package it.polimi.ingsw.model.updates;

import java.io.Serializable;

public abstract class PlayerSpecificUpdate implements Serializable {
    public final String playerID;

    PlayerSpecificUpdate(String playerID) {
        this.playerID = playerID;
    }
}
