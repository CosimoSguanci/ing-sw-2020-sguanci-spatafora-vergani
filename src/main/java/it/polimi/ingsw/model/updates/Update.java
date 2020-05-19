package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

public abstract class Update implements Serializable {
    private final String currentPlayerID;

    /**
     * JSON Serialized Board representation
     */
    private final String board;

    //public Update() {}

    public Update(String currentPlayerID, String board) {
        this.currentPlayerID = currentPlayerID;
        this.board = board;
    }

    public String getCurrentPlayerID() {
        return this.currentPlayerID;
    }

    public String getBoard() {
        return this.board;
    }

    public abstract void handleUpdate(UpdateHandler handler);
}
