package it.polimi.ingsw.model.updates;


public abstract class PlayerSpecificUpdate extends Update {
    public final String playerID;

    PlayerSpecificUpdate(String playerID) {
        this.playerID = playerID;
    }
}
