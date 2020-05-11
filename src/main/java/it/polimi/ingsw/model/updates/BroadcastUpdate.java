package it.polimi.ingsw.model.updates;

public abstract class BroadcastUpdate extends Update {

    /**
     * JSON Serialized Board representation.
     */
    public final String board;

    BroadcastUpdate(String board) {
        this.board = board;
    }

}
