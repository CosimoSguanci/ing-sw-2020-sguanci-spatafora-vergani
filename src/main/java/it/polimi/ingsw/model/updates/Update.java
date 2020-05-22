package it.polimi.ingsw.model.updates;

import it.polimi.ingsw.view.UpdateHandler;

import java.io.Serializable;

public abstract class Update implements Serializable {
    private static final long serialVersionUID = 8108733601179952266L;

    /**
     * JSON Serialized Board representation
     */
    private final String board;

    public Update(String board) {
        this.board = board;
    }

    public String getBoard() {
        return this.board;
    }

    public abstract void handleUpdate(UpdateHandler handler);
}
