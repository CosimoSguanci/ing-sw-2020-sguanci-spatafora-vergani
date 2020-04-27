package it.polimi.ingsw.model.updates;

import java.io.Serializable;

public abstract class BroadcastUpdate extends Update implements Serializable {

    /**
     * JSON Serialized Board representation.
     */
    public final String board;

    BroadcastUpdate(String board) {
        this.board = board;
    }
}
