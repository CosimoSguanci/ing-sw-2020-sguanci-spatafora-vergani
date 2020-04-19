package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;

public class ModelUpdate {
    private Board board;

    public ModelUpdate(Board board) {
        this.board = board;
    }

    public Board getBoard(){
        return this.board;
    }

}
