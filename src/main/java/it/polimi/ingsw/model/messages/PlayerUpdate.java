package it.polimi.ingsw.model.messages;

import java.io.Serializable;

public class PlayerUpdate implements Serializable {
    private String playerID;

    public PlayerUpdate(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerID() {
        return this.playerID;
    }
}
