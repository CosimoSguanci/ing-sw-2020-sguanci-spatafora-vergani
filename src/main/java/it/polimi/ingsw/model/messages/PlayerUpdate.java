package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Player;

public class PlayerUpdate {
    private Player player;

    PlayerUpdate(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }
}
