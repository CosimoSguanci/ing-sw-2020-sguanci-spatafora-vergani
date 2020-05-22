package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

public abstract class Update implements Serializable {
    private static final long serialVersionUID = 8108733601179952266L;
    private final Player currentPlayer;

    /**
     * JSON Serialized Board representation
     */
    private final String board;

    //public Update() {}

    public Update(Player currentPlayer, String board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    } // todo remove currentplayer here?

    public String getBoard() {
        return this.board;
    }

    public abstract void handleUpdate(UpdateHandler handler);
}
