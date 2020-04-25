package it.polimi.ingsw.model.messages;


import java.io.Serializable;

public class TurnUpdate implements Serializable {
    public final String playerID;

    public TurnUpdate(String playerID) {
        this.playerID = playerID;
    }
}
