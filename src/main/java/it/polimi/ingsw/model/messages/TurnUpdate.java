package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Player;

public class TurnUpdate {
    private Player player;

    public TurnUpdate(Player player) {
        this.player = player;
    }

    public Player getCurrentPlayer() {
        return this.player;
    }
}
