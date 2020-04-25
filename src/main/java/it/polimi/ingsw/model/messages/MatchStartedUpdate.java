package it.polimi.ingsw.model.messages;


import java.io.Serializable;

public class MatchStartedUpdate implements Serializable {
    public final String board;

    public MatchStartedUpdate(String board) {
        this.board = board;
    }
}
