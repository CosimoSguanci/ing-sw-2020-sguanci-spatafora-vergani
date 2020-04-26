package it.polimi.ingsw.model.messages;

import java.io.Serializable;

public class GamePreparationUpdate implements Serializable {
    public final String player; // TODO remove Player
    public final String board; // TODO GSON

    public GamePreparationUpdate(String player, String board) {
        this.player = player;
        this.board = board;
    }
}
