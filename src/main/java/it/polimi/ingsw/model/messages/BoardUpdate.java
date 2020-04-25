package it.polimi.ingsw.model.messages;

import java.io.Serializable;

public class BoardUpdate implements Serializable {
    public final String board;

    public BoardUpdate(String board) {
        this.board = board;
    }
}
