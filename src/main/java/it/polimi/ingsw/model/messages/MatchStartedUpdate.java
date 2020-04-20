package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;

public class MatchStartedUpdate {
    private Board board;

    public MatchStartedUpdate(Board board) {
        this.board = board;
    }

    public Board getBoard(){
        return this.board;
    }

}
