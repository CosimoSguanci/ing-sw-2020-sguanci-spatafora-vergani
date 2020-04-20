package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Player;

public class TurnUpdate {
    private String playerID;

    public TurnUpdate(String playerID) {
        this.playerID = playerID;
    }

    public String getCurrentPlayer() {
        return this.playerID;
    }
}
